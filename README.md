# bael
[![](https://jitpack.io/v/promicx/bael.svg)](https://jitpack.io/#promicx/bael)

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.promicx:bael:2.9-beta'
	}
