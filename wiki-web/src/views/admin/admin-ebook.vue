<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div style="margin-bottom: 10px">
        <a-space>
          <a-input-search v-model:value="searchBookName"
                          allow-clear
                          :loading="searching"
                          enter-button
                          placeholder="电子书名称"
                          @search="handleSearchBookName"
          >
            <template #prefix>
              <book-two-tone/>
            </template>
            <template #enter-button>

            </template>
          </a-input-search>
          <a-button type="primary" @click="add">
            添加
          </a-button>
        </a-space>
      </div>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #bodyCell="{ column, text, record }">
          <template v-if="column.dataIndex === 'cover'">
            <img :src="record.cover" alt="封面"/>
          </template>
          <template v-else-if="column.dataIndex === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button type="primary" danger>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal v-model:open="formOpen" title="电子书表单" :confirm-loading="formLoading" @ok="handleFormOk">
    <a-form
        :model="ebook"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
    >
      <a-form-item
          label="封面"
          name="cover"
      >
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>

      <a-form-item
          label="名称"
          name="name"
          :rules="[{ required: true, message: '请输入书名' }]"
      >
        <a-input v-model:value="ebook.name"/>
      </a-form-item>

      <a-form-item
          label="分类"
          name="categories"
      >
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{label: 'name', value: 'id', children: 'children'}"
            :options="level1"
            placeholder="请选择分类"
        />
      </a-form-item>

      <a-form-item
          label="描述"
          name="description"
      >
        <a-input v-model:value="ebook.description"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script setup lang="ts">

import {defineComponent, h, onMounted, ref} from "vue";
import axios from "axios";
import {notification, NotificationPlacement} from "ant-design-vue";
import {CloseCircleFilled} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";

defineComponent({
  name: "AdminEbook"
})

const ebooks = ref();

const loading = ref(true);

const columns = [
  {
    title: "封面",
    dataIndex: "cover",
  },
  {
    title: "名称",
    dataIndex: "name"
  },
  {
    title: "分类一",
    key: "category1Id",
    dataIndex: "category1Id",
  },
  {
    title: "分类二",
    dataIndex: "category2Id",
  },
  {
    title: "文档数",
    dataIndex: "docCount",
  },
  {
    title: "阅读数",
    dataIndex: "viewCount",
  },
  {
    title: "点赞数",
    dataIndex: "voteCount",
  },
  {
    title: "操作",
    dataIndex: "action"
  }
];

/**
 * 表单
 */
const ebook: any = ref();
const formOpen = ref(false);
const formLoading = ref(false);
const categoryIds = ref();
const handleFormOk = () => {

  ebook.value.category1Id = categoryIds.value[0];
  ebook.value.category2Id = categoryIds.value[1];

  formLoading.value = true;
  axios.post("/ebook/save", {
    ...ebook.value
  }).then(res => {
    if (res.data.success) {
      formLoading.value = false;
      formOpen.value = false;

      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    } else {
      formLoading.value = false;
    }
  })
}

/**
 * 编辑
 */
const edit = (record: any) => {
  formOpen.value = true;
  ebook.value = Tool.copy(record);
  categoryIds.value = [ebook.value.category1Id.toString(), ebook.value.category2Id.toString()];
}

/**
 * 添加
 */
const add = () => {
  formOpen.value = true;
  ebook.value = {};
}

/**
 * 删除
 */
const handleDelete = (id: number) => {
  axios.delete(`/ebook/delete/${id}`).then(res => {
    if (res.data.success) {
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    }
  });
};

/**
 * 书名搜索
 */
const searchBookName = ref("");
const searching = ref(false);
const handleSearchBookName = () => {
  if (searching.value === true) {
    return;
  }
  searching.value = true;
  handleQuery({
    page: 1,
    size: pagination.value.pageSize,
    name: searchBookName.value
  }).then(() => {
    searching.value = false;
  })
}

/**
 * 查询请求
 *
 * @param params
 */
const handleQuery = (params: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/ebook/list", {
      params
    }).then(res => {
      loading.value = false;

      if (!res.data.success) {
        return;
      }

      ebooks.value = res.data.content.list;

      pagination.value.current = params.page;
      pagination.value.total = res.data.content.list;

      resolve(res);
    })
  })
}

const category = ref();
const level1 = ref();
const handleQueryAll = (params?: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/category/all", {
      params
    }).then(res => {
      loading.value = false;

      if (!res.data.success) {
        return;
      }

      const categorys = res.data.content;

      level1.value = Tool.array2Tree(categorys, categorys[0].parent);

      resolve(res);
    })
  })
}

/**
 * 分页
 */
const pagination = ref({
  current: 1,
  pageSize: 1000,
  total: 0
});
const handleTableChange = (pagination: { current: number, pageSize: number }) => {
  console.log("自带分页参数 : " + pagination);
  handleQuery({
    page: pagination.current,
    size: pagination.pageSize
  });
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  });
  handleQueryAll();
});

</script>