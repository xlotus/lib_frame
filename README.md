# lib_frame
Android 基础库

### 开始使用
1. 在 project 的 build.gradle 文件中找到 allprojects{} 代码块添加以下代码：

    allprojects {
   
        repositories {
   
            google()
       
            jcenter()
    
            maven { url 'https://jitpack.io'}  //增加 jitPack Maven 仓库
        }
    }

2. 在 app 的 build.gradle 文件中找到 dependencies{} 代码块，并在其中加入以下语句：

   dependencies {
   
       implementation "com.github.xlotus:lib_frame:1.0.0"
   
   }
