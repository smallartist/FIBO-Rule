<script>
export default {
  created() {
    const { params, query } = this.$route
    console.log(params);
    const { path } = params
    this.$router.replace({ path: '/' + path, query })
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
