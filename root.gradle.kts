import gg.essential.gradle.util.*

plugins {
  id("gg.essential.multi-version.root")
}

preprocess {
  val fabric11204 = createNode("1.20.4-fabric", 12004, "yarn")
  val fabric11201 = createNode("1.20.1-fabric", 12001, "yarn")
  val fabric11904 = createNode("1.19.4-fabric", 11904, "yarn")

  fabric11204.link(fabric11201)
  fabric11201.link(fabric11904)
}