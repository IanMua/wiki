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
                          placeholder="文档名称"
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
          :data-source="level1"
          :loading="loading"
          :pagination="false"
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
  <a-modal v-model:open="formOpen" title="文档表单" :confirm-loading="formLoading" @ok="handleFormOk" :maskClosable="false" style="min-width: 50vw;">
    <a-form
        :model="doc"
        name="basic"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 16 }"
    >

      <a-form-item
          label="名称"
          name="name"
          :rules="[{ required: true, message: '请输入文档名称' }]"
      >
        <a-input v-model:value="doc.name"/>
      </a-form-item>

      <a-form-item
          label="父文档"
          name="name"
          :rules="[{ required: true, message: '请输入文档名称' }]"
      >
        <a-tree-select
            v-model:value="doc.parent"
            show-search
            style="width: 100%"
            :field-names="{label: 'name', value: 'id', children: 'children'}"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择父文档"
            allow-clear
            tree-default-expand-all
            :tree-data="treeSelectData"
            tree-node-filter-prop="label"
        />
      </a-form-item>

      <a-form-item
          label="顺序"
          name="sort"
          :rules="[{ required: true, message: '请输入排序号' }]"
      >
        <a-input v-model:value="doc.sort"/>
      </a-form-item>

      <a-form-item
          label="内容"
          name="content"
          :rules="[{ required: true, message: '请输入内容' }]"
      >
        <Editor v-model="content" :api-key="apiKey" :init="init" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script setup lang="ts">

import {defineComponent, h, onMounted, reactive, ref} from "vue";
import axios from "axios";
import {notification, NotificationPlacement} from "ant-design-vue";
import {CloseCircleFilled} from "@ant-design/icons-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import Editor from "@tinymce/tinymce-vue";

defineComponent({
  name: "AdminDoc"
})

const route = useRoute();

const docs = ref();

const loading = ref(true);

const columns = [
  {
    title: "名称",
    dataIndex: "name"
  },
  {
    title: "父文档",
    key: "parent",
    dataIndex: "parent",
  },
  {
    title: "顺序",
    dataIndex: "sort",
  },
  {
    title: "操作",
    dataIndex: "action"
  }
];

/**
 * 表单
 */
const doc: any = ref();
const formOpen = ref(false);
const formLoading = ref(false);
const treeSelectData = ref();
const handleFormOk = () => {
  formLoading.value = true;
  axios.post("/doc/save", {
    ...doc.value
  }).then(res => {
    if (res.data.success) {
      formLoading.value = false;
      formOpen.value = false;

      handleQuery({
        ebookId: route.query.ebookId
      });
    } else {
      formLoading.value = false;
    }
  })
}
const setDisable = (treeSelectData: any, id: any) => {
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      node.disabled = true;

      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          setDisable(children, children[j].id);
        }
      }
    }
  }
}

const ids: Array<string> = [];
const getDeleteIds = (treeSelectData: any, id: any) => {
  for (let i = 0; i < treeSelectData.length; i++) {
    const node = treeSelectData[i];
    if (node.id === id) {
      ids.push(id);

      const children = node.children;
      if (Tool.isNotEmpty(children)) {
        for (let j = 0; j < children.length; j++) {
          getDeleteIds(children, children[j].id);
        }
      }
    }
  }
}

/**
 * 富文本初始化
 */
const content = ref();
const apiKey = ref("60t8v3pn12b662p8h2qrolgh0sr9jdmjkxctsdur4dgonm9o");
const init = reactive({
  language: "zh_CN", //语言类型
  placeholder: "在这里输入文字",
  // min_width: 600,
  min_height: 600,
  // height: 600, //注：引入autoresize插件时，此属性失效
  resize: "both", //编辑器宽高是否可变，false-否,true-高可变，'both'-宽高均可，注意引号
  branding: false, //tiny技术支持信息是否显示
  // statusbar: false,  //最下方的元素路径和字数统计那一栏是否显示
  elementpath: false, //元素路径是否显示

  // 字体样式
  font_size_formats: '11px 12px 14px 16px 18px 24px 36px 48px',
  font_family_formats: "微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;",
  // 插件配置 axupimgs indent2em
  plugins: "preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap pagebreak nonbreaking anchor insertdatetime advlist lists wordcount autosave emoticons",
  //工具栏配置，设为false则隐藏
  toolbar: [
    "fullscreen undo redo restoredraft | forecolor backcolor bold italic underline strikethrough link anchor | alignleft aligncenter alignright alignjustify outdent indent | bullist numlist | blockquote subscript superscript removeformat ",
    "fontfamily fontsize styles | table image axupimgs media emoticons charmap hr pagebreak insertdatetime  selectall visualblocks searchreplace | code print preview | indent2em lineheight formatpainter "
  ],
  //菜单栏配置，设为false则隐藏，不配置则默认显示全部菜单，也可自定义配置--查看 http://tinymce.ax-z.cn/configure/editor-appearance.php --搜索“自定义菜单”
  // menubar: "file edit my1",
  // menubar: false,

})


/**
 * 编辑
 */
const edit = (record: any) => {
  formOpen.value = true;
  doc.value = Tool.copy(record);

  treeSelectData.value = Tool.copy(level1.value);
  setDisable(treeSelectData.value, record.id);
  treeSelectData.value.unshift({id: 0, name: "无"});
}

/**
 * 添加
 */
const add = () => {
  formOpen.value = true;
  doc.value = {
    ebookId: route.query.ebookId
  };

  treeSelectData.value = Tool.copy(level1.value);
  treeSelectData.value.unshift({id: 0, name: "无"});
}

/**
 * 删除
 */
const handleDelete = (id: number) => {
  getDeleteIds(level1.value, id);
  axios.delete("/doc/delete/" + ids.join(",")).then(res => {
    if (res.data.success) {
      handleQuery({
        ebookId: route.query.ebookId
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
    name: searchBookName.value,
    ebookId: route.query.ebookId
  }).then(() => {
    searching.value = false;
  })
}

const level1 = ref();

/**
 * 查询请求
 */
const handleQuery = (params?: any) => {
  return new Promise((resolve, reject) => {
    loading.value = true;
    axios.get("/doc/all", {
      params
    }).then(res => {
      loading.value = false;

      if (!res.data.success) {
        return;
      }

      docs.value = res.data.content;

      level1.value = Tool.array2Tree(docs.value, 0);

      resolve(res);
    })
  })
}

onMounted(() => {
  handleQuery({
    ebookId: route.query.ebookId
  });

});

</script>