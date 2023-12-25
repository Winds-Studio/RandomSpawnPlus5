plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1" apply true
}

group = "systems.kscott"
version = "5.1.0"

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
}

val adventureVersion = "4.15.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    compileOnly("org.apache.commons:commons-lang3:3.14.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    api("org.bstats:bstats-bukkit:3.0.2")
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    api("com.tcoded:FoliaLib:0.3.1")

    compileOnly("net.essentialsx:EssentialsX:2.20.1")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    api("net.kyori:adventure-platform-bukkit:4.3.1")
    api("net.kyori:adventure-api:$adventureVersion")
    api("net.kyori:adventure-text-serializer-legacy:$adventureVersion")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.build.configure {
    dependsOn("shadowJar")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveFileName = "${project.name}-${project.version}.jar"
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

tasks {
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

