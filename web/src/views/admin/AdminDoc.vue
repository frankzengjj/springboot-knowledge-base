<template>
  <a-layout>
    <a-layout-content
      :style="{
        background: '#fff',
        padding: '24px',
        margin: 0,
        minHeight: '280px',
      }"
    >
      <a-row :gutter="24">
        <a-col :span="8">
          <!-- <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="add()"> Add </a-button>
              </a-form-item>
            </a-form>
          </p> -->
          <a-table
            v-if="level1.length > 0"
            :columns="columns"
            :row-key="(record) => record.id"
            :data-source="level1"
            :loading="loading"
            :pagination="false"
            size="small"
            :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{ record.sort }} {{ text }}
            </template>
            <template v-slot:action="{ record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  Edit
                </a-button>
                <a-popconfirm
                  title="Are you sure delete this entry?"
                  ok-text="Yes"
                  cancel-text="No"
                  @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small"> Delete </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <a-form :model="doc" layout="vertical">
            <p>
              <a-form layout="inline" :model="param">
                <a-form-item>
                  <a-button type="primary" @click="add()"> Clear All </a-button>
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" @click="handleSave()">
                    Save
                  </a-button>
                </a-form-item>
                <a-form-item>
                  <a-button type="primary" @click="handlePreviewContent()">
                    <EyeOutlined /> Preview
                  </a-button>
                </a-form-item>
              </a-form>
            </p>
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="Name" />
            </a-form-item>
            <a-form-item>
              <a-tree-select
                v-model:value="doc.parent"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeSelectData"
                placeholder="Please select parent doc"
                tree-default-expand-all
                :replaceFields="{ title: 'name', value: 'id' }"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="Order" />
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
      <a-drawer
        width="900"
        placement="right"
        :closable="false"
        :visible="drawerVisible"
        @close="onDrawerClose"
      >
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>
    </a-layout-content>
  </a-layout>
  <!-- <a-modal
    title="Title"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >

  </a-modal> -->
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, createVNode } from "vue";
import axios from "axios";
import { message, Modal } from "ant-design-vue";
import { Tool } from "@/util/tool";
import { useRoute } from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from "wangeditor";
import i18next from 'i18next';

export default defineComponent({
  name: "AdminDoc",
  setup() {
    const route = useRoute();
    const docs = ref();
    const param = ref();
    param.value = {};
    const loading = ref(false);
    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [
      {
        title: "名称",
        dataIndex: "name",
        slots: { customRender: "name" },
      },
      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" },
      },
    ];

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      level1.value = [];
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          treeSelectData.value =
            level1.value.length === 0 ? [] : Tool.copy(level1.value);
          treeSelectData.value.unshift({ id: 0, name: "None" });
        } else {
          message.error(data.message);
        }
      });
    };

    const handleQueryContent = () => {
      axios.get("/doc/content/" + doc.value.id).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const idsToDelete: Array<string> = [];
    const deleteNames: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          idsToDelete.push(id);
          deleteNames.push(node.name);
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId,
    };
    const level1 = ref();
    level1.value = [];
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const editor = new E("#content");
    editor.config.zIndex = 0;
    editor.config.uploadImgShowBase64 = true;
    const handleSave = () => {
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        const data = response.data; // data == commonresponse in the backend
        modalLoading.value = false;
        if (data.success) {
          message.success("Saved");
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);
      treeSelectData.value.unshift({ id: 0, name: "None" });
    };

    const add = () => {
      editor.txt.html("");
      doc.value = {
        ebookId: route.query.ebookId,
      };
      treeSelectData.value =
        level1.value.length === 0 ? [] : Tool.copy(level1.value);
      treeSelectData.value.unshift({ id: 0, name: "None" });
    };

    const handleDelete = (id: number) => {
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: "Warning",
        icon: createVNode(ExclamationCircleOutlined),
        content:
          "Will Delete：【" + deleteNames.join("，") + "】Confirm to delete？",
        onOk() {
          // console.log(ids)
          axios
            .delete("/doc/delete/" + idsToDelete.join(","))
            .then((response) => {
              const data = response.data; // data = commonResp
              if (data.success) {
                // 重新加载列表
                handleQuery();
              }
            });
        },
      });
    };

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    onMounted(() => {
      editor.config.lang = 'en';
      editor.i18next = i18next;
      editor.create();
      handleQuery();
    });

    return {
      param,
      docs,
      columns,
      loading,
      edit,
      add,
      handleDelete,
      handleQuery,
      modalVisible,
      modalLoading,
      handleSave,
      doc,
      level1,
      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    };
  },
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>