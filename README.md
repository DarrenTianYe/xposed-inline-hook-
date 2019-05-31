# xposed-inline-hook-

优点：

1. xposed 中集成了inlinehook ， 可以在应用启动时hook 目标应用的相关native 函数。
2. xposed 工程修改后不用重新启动。

操作步骤：

1. 修改UusafeHookTest.java   确定需要hook 的包。
2. 将inline hook 的so 加载进入 目标进程。（参见代码）
3. 在 main.c  JNI_OnLoad(或者更早的构造函数)中的hooker.c  hook 相关的函数。 
4. hook 完后可以调用原函数实现链式， 也可以取消hook 。


配置：
1. 使用cmake 配置和编译so
2. so 路径： app/build/intermediates/cmake/debug/obj/armeabi-v7/