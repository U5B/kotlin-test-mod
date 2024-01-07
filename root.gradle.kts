import gg.essential.gradle.util.*

plugins {
  id("gg.essential.multi-version.root")
}

preprocess {
  val fabric11206 = createNode("1.20.6-fabric", 12006, "yarn")
  val fabric11204 = createNode("1.20.4-fabric", 12004, "yarn")
  val fabric11202 = createNode("1.20.2-fabric", 12002, "yarn")
  val fabric11201 = createNode("1.20.1-fabric", 12001, "yarn")
  val fabric11904 = createNode("1.19.4-fabric", 11904, "yarn")

  fabric11206.link(fabric11204)
  fabric11204.link(fabric11202)
  fabric11202.link(fabric11201)
  fabric11201.link(fabric11904)
}