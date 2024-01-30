class ComposePlugin : DependencyOnlyPlugin() {
    override fun addLibraryNames(): List<String> {
        return listOf(
            "compose.activity",
            "compose.ui",
            "compose.uiTooling",
            "compose.foundation",
            "compose.viewModel",
            "compose.material3"
        )
    }
}
