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
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-button type="primary" @click="add()"> Add </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
        :columns="columns"
        :row-key="(record) => record.id"
        :data-source="level1"
        :loading="loading"
        :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)"> Edit </a-button>
            <a-popconfirm
              title="Are you sure delete this entry?"
              ok-text="Yes"
              cancel-text="No"
              @confirm="handleDelete(record.id)"
            >
              <a-button type="danger"> Delete </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
    title="Title"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form
      :model="category"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
    >
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="Sort">
        <a-input v-model:value="category.sort" />
      </a-form-item>
      <a-form-item label="Parent Category">
        <a-select v-model:value="category.parent" ref="select">
          <a-select-option value="0">None</a-select-option>
          <a-select-option
            v-for="c in level1"
            :key="c.id"
            :value="c.id"
            :disabled="category.id === c.id"
            >{{ c.name }}</a-select-option
          >
        </a-select>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

export default defineComponent({
  name: "AdminCategory",
  setup() {
    const categories = ref();
    const param = ref();
    param.value = {};
    const loading = ref(false);

    const columns = [
      {
        title: "名称",
        dataIndex: "name",
      },
      {
        title: "Order",
        dataIndex: "sort",
      },
      {
        title: "Parent Category",
        dataIndex: "parent",
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
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categories.value = data.content;
          level1.value = [];
          level1.value = Tool.array2Tree(categories.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };

    const category = ref({});
    const level1 = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        const data = response.data; // data == commonresponse in the backend
        modalLoading.value = false;
        if (data.success) {
          modalVisible.value = false;
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
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    const handleDelete = (id: number) => {
      console.log("id is", id);
      axios.delete("/category/delete/" + id).then((response) => {
        const data = response.data; // data == commonresponse in the backend
        if (data.success) {
          handleQuery();
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      categories,
      columns,
      loading,
      edit,
      add,
      handleDelete,
      handleQuery,
      modalVisible,
      modalLoading,
      handleModalOk,
      category,
      level1,
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