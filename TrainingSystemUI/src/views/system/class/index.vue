<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryRef"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="课程名称" prop="className">
        <el-input
          v-model="queryParams.className"
          placeholder="请输入课程名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="课程状态"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in training_class_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:dept:add']"
          >新增</el-button
        >
      </el-col>
      <right-toolbar
        v-model:showSearch="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="classList"
      row-key="classId"
      :default-expand-all="isExpandAll"
    >
      <el-table-column type="index" label="序号" width="50" />
      <el-table-column
        prop="className"
        label="课程名称"
        width="200"
      ></el-table-column>
      <el-table-column
        prop="teacherName"
        label="讲师"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="location"
        label="上课地点"
        width="200"
      ></el-table-column>
      <el-table-column prop="classBeginTime" label="课程开始时间" width="200">
        <template #default="scope">
          <span>{{ parseTime(scope.row.classBeginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="课程状态" width="100">
        <template #default="scope">
          <dict-tag
            :options="training_class_status"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdateStatus(scope.row)"
            v-hasPermi="['system:dept:edit']"
            >改变状态</el-button
          >
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dept:edit']"
            >修改</el-button
          >
          <el-button
            link
            type="primary"
            icon="Plus"
            @click="handleAdd()"
            v-hasPermi="['system:dept:add']"
            >新增</el-button
          >
          <el-button
            v-if="scope.row.parentId != 0"
            link
            style="color: red"
            type="primary"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dept:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改课程对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="classRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="22">
            <el-form-item label="课程名称" prop="className">
              <el-input v-model="form.className" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="讲师" prop="teacherName">
              <el-input
                v-model="form.teacherName"
                placeholder="请输入讲师姓名"
                maxlength="20"
              />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="上课地点" prop="location">
              <el-input
                v-model="form.location"
                placeholder="请输入上课地点"
                maxlength="500"
              />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="开课时间" prop="classBeginTimeStr">
              <el-date-picker
                v-model="form.classBeginTimeStr"
                type="datetime"
                placeholder="请选择开课时间"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="课程内容" prop="content">
              <el-input
                v-model="form.content"
                style="width: 100%"
                :rows="8"
                type="textarea"
                placeholder="请输入课程内容"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 修改课程状态对话框 -->
    <el-dialog
      title="修改课程状态"
      v-model="statusOpen"
      width="400px"
      append-to-body
    >
      <el-form ref="statusRef" :model="status" label-width="80px">
        <el-row>
          <el-col :span="22">
            <el-form-item label="课程状态" prop="className">
              <el-select
                v-model="status"
                placeholder="Select"
                size="large"
                style="width: 240px"
              >
                <el-option
                  v-for="item in training_class_status"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="changeStatus">确 定</el-button>
          <el-button @click="cancelStatus">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Class">
import {addClass, delClass, getClass, listClass, updateClass,} from "@/api/system/class";

const { proxy } = getCurrentInstance();
const { training_class_status } = proxy.useDict("training_class_status");

const classList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const title = ref("");
const isExpandAll = ref(true);
const refreshTable = ref(true);
const status = ref("");
const statusOpen = ref(false);
const currentClassId = ref(null);

const data = reactive({
  form: {},
  queryParams: {
    className: undefined,
    teacherName: undefined,
    queryBeginTime: undefined,
    queryEndTime: undefined,
    status: undefined,
  },
  rules: {
    className: [
      { required: true, message: "课程名称不能为空", trigger: "blur" },
    ],
    teacherName: [
      { required: true, message: "讲师姓名不能为空", trigger: "blur" },
    ],
    location: [
      { required: true, message: "上课地点不能为空", trigger: "blur" },
    ],
    classBeginTimeStr: [
      { required: true, message: "开课时间不能为空", trigger: "blur" },
    ],
    content: [{ required: true, message: "课程内容不能为空", trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 查询课程列表 */
function getList() {
  loading.value = true;
  listClass(queryParams.value).then((response) => {
    classList.value = response.data;
    loading.value = false;
  });
}

/** 取消按钮 */
function cancel() {
  open.value = false;
  reset();
}

/** 取消修改状态按钮 */
function cancelStatus() {
  statusOpen.value = false;
}

/** 表单重置 */
function reset() {
  form.value = {
    classId: undefined,
    parentId: undefined,
    deptName: undefined,
    orderNum: 0,
    leader: undefined,
    phone: undefined,
    email: undefined,
    status: "0",
  };
  proxy.resetForm("classRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加课程";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  getClass(row.classId).then((response) => {
    form.value = response.data;
    form.value.classBeginTimeStr = response.data.classBeginTime;
    open.value = true;
    title.value = "修改课程";
  });
}

/** 修改状态按钮操作 */
function handleUpdateStatus(row) {
  statusOpen.value = true;
  currentClassId.value = row.classId;
  status.value = row.status;
}

/** 修改状态 */
function changeStatus() {
  updateClass({ classId: currentClassId.value, status: status.value }).then(
    () => {
      statusOpen.value = false;
      proxy.$modal.msgSuccess("修改成功");
      open.value = false;
      getList();
    }
  );
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["classRef"].validate((valid) => {
    if (valid) {
      form.value.classBeginTime = null;
      if (form.value.classId != undefined) {
        updateClass(form.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addClass(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  proxy.$modal
    .confirm('是否确认删除课程【"' + row.className + '"】?')
    .then(function () {
      return delClass(row.classId);
    })
    .then(() => {
      getList();
      proxy.$modal.msgSuccess("删除成功");
    })
    .catch(() => {});
}

getList();
</script>
