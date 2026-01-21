plugins {
    id("java")
}

group = "cc.irori"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.irori.cc/repository/public/")
    maven("https://maven.hytale-modding.info/releases/")
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    compileOnly(libs.shodo)
    compileOnly(libs.multiplehud)
}
