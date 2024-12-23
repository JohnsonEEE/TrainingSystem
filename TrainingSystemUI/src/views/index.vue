<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>欢迎您使用考试培训系统</h2>
        <p>
          随着企业培训需求的日益增长，传统的人工记录和管理方式效率低下，难以满足现代企业对培训管理的高效性、便捷性和可视化需求。本系统旨在通过信息化手段，为企业内部培训提供一站式管理，覆盖从课程发布、员工报名、培训记录到反馈评估的全流程功能，提升企业培训管理效率。
        </p>
      </el-col>
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>我们的目标</h2>
        <p>
          <li>
            提升培训效率：为企业提供一个高效、灵活的内部培训管理平台，实现培训课程的全流程数字化管理，包括课程创建、员工报名、进度跟踪及反馈评估，提高管理效率和执行效果。
          </li>
          <li>
            优化培训体验：通过智能化功能（如课程推荐、反馈分析等），优化员工培训体验，提升培训参与度和课程满意度，为企业培养更多高素质人才。
          </li>
          <li>
            数据驱动决策：提供全面的数据统计与分析功能，包括培训报名率、出勤率、反馈统计等，为企业的培训效果评估和后续决策提供数据支持。
          </li>
          <li>
            支持灵活扩展：系统采用模块化设计和主流技术架构，方便企业根据业务需求进行功能扩展，支持企业未来的多样化培训场景。
          </li>
        </p>
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>

<script setup name="Index"></script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
