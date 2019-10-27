const core = require('@actions/core')
const { modVersion, minecraftVersion } = require(process.env.GITHUB_WORKSPACE + 'version.json')
core.setOutput('modversion', modVersion)
core.setOutput('mcversion', minecraftVersion)