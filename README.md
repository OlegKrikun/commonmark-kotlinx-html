# commonmark-kotlinx-html

Small library that provides an ability to render [commonmark-java](https://github.com/atlassian/commonmark-java) parsing result into [kotlinx.html](https://github.com/Kotlin/kotlinx.html) stream

[![Download](https://api.bintray.com/packages/olegkrikun/maven/commonmark-kotlinx-html/images/download.svg)](https://bintray.com/olegkrikun/maven/commonmark-kotlinx-html/_latestVersion)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.10-orange.svg)](https://kotlinlang.org/)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-green.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

## KotlinxHtmlRenderer

The main part of this library.

For general information please read `commonmark-java` [readme](https://github.com/atlassian/commonmark-java)

### Get

```kotlin
repositories {
    jcenter()
}

dependencies {
    implementation("ru.krikun.commonmark:commonmark-kotlinx-html:0.1.1")
}
```

### Usage

```kotlin
createHTML().html {
    body {
        val document: Document = ... // Parse document using commonmark-java parser.
        val renderer = buildKotlinxHtmlRenderer()

        renderer.render(document, stream)
    }
} 
```

or use kotlin extension function:
```kotlin
div {
    val document: Document = ... // Parse document using commonmark-java parser.
    render(document)
}
```

## Extension

For general information about extensions see `commonmark-java` [readme](https://github.com/atlassian/commonmark-java#extensions)

**Note**: `commonmark-kotlinx-html` extensions use internal implementation of parsing part from original `commonmark-java` extensions.
So you don't need add original extension to the parser. Use `commonmark-kotlinx-html` extension instead.

### Heading anchor

Enables adding auto generated "id" attributes to heading tags. 

Use `HeadingAnchorKotlinxHtmlExtension`

```kotlin
dependencies {
    implementation("ru.krikun.commonmark:commonmark-kotlinx-html-heading-anchor:0.1.1")
}
```
Also, see `commonmark-java` [description](https://github.com/atlassian/commonmark-java#heading-anchor).

### Ins

Enables underlining of text by enclosing it in `++`. 

Use `InsKotlinxHtmlExtension`

```kotlin
dependencies {
    implementation("ru.krikun.commonmark:commonmark-kotlinx-html-ins:0.1.1")
}
```
Also, see `commonmark-java` [description](https://github.com/atlassian/commonmark-java#ins).


### Strikethrough

Enables strikethrough of text by enclosing it in `~~`.

Use `StrikethroughKotlinxHtmlExtension`.

```kotlin
dependencies {
    implementation("ru.krikun.commonmark:commonmark-kotlinx-html-strikethrough:0.1.1")
}
```
Also, see `commonmark-java` [description](https://github.com/atlassian/commonmark-java#strikethrough).

### Tables

Enables tables using pipes as in GitHub Flavored Markdown.

Use `TablesKotlinxHtmlExtension`

```kotlin
dependencies {
    implementation("ru.krikun.commonmark:commonmark-kotlinx-html-tables:0.1.1")
}
```
Also, see `commonmark-java` [description](https://github.com/atlassian/commonmark-java#tables).

### Dependencies
Dependency      | Version
--------------- | :----:
kotlin-jvm      | 1.4.10
kotlinx.html    | 0.7.2
commonmark-java | 0.15.2

## License

```
Copyright 2020 OlegKrikun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
