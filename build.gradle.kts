plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
}

group = "systems.kscott"
version = "6.0.0"

repositories {
    mavenCentral()

    // PaperMC
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    // EssentialsX
    maven {
        name = "ess-repo"
        url = uri("https://repo.essentialsx.net/releases/")
    }

    // acf-paper
    maven {
        name = "aikar-repo"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }

    // JitPack
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io")
    }

    // FoliaLib
    maven {
        name = "devmart-other"
        url = uri("https://nexuslite.gcnt.net/repos/other/")
    }

    // ConfigurationMaster API
    maven {
        name = "ConfigurationMaster-repo"
        url = uri("https://ci.pluginwiki.us/plugin/repository/everything/")
    }
}

val adventureVersion = "4.17.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")

    compileOnly("org.apache.logging.log4j:log4j-api:2.24.1")
    compileOnly("it.unimi.dsi:fastutil:8.5.15")
    api("org.bstats:bstats-bukkit:3.1.0")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT") // Remove
    api("com.tcoded:FoliaLib:0.4.2")
    implementation("com.github.thatsmusic99:ConfigurationMaster-API:v2.0.0-rc.2")

    compileOnly("net.essentialsx:EssentialsX:2.20.1")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    api("net.kyori:adventure-platform-bukkit:4.3.4")
    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    build.configure {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveFileName = "${project.name}-${project.version}.${archiveExtension.get()}"
        exclude("META-INF/**", // Dreeam - Avoid to include META-INF/maven in Jar
            "com/cryptomorin/xseries/XBiome*",
            "com/cryptomorin/xseries/NMSExtras*",
            "com/cryptomorin/xseries/NoteBlockMusic*",
            "com/cryptomorin/xseries/SkullCacheListener*")
        minimize {
            exclude(dependency("com.tcoded.folialib:.*:.*"))
        }
        relocate("net.kyori", "systems.kscott.randomspawnplus.libs.kyori")
        relocate("co.aikar.commands", "systems.kscott.randomspawnplus.libs.acf.commands")
        relocate("co.aikar.locales", "systems.kscott.randomspawnplus.libs.acf.locales")
        relocate("com.cryptomorin.xseries", "systems.kscott.randomspawnplus.libs.xseries")
        relocate("org.bstats", "systems.kscott.randomspawnplus.libs.bstats")
        relocate("com.tcoded.folialib", "systems.kscott.randomspawnplus.libs.folialib")
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand("version" to project.version)
        }
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

