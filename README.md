# LiveEventBus
![license](https://img.shields.io/github/license/JeremyLiao/LiveEventBus.svg) [![version](https://img.shields.io/badge/JCenter-v1.2.2-blue.svg)](https://mvnrepository.com/artifact/com.jeremyliao/live-event-bus)

LiveEventBus是一款Android消息总线，基于LiveData，具有生命周期感知能力，支持Sticky

![logo](/images/logo.png)

## 简单之美
[LiveEventBus](/live-event-bus/liveeventbus/src/main/java/com/jeremyliao/liveeventbus/LiveEventBus.java)实现非常简单，功能却非常强大。简单好用，就是最好的：）

## LiveEventBus的特点
- [x] 生命周期感知，消息随时订阅，自动取消订阅
- [x] 支持Sticky粘性消息
- [x] 支持设置LifecycleObserver（如Activity）接收消息的模式：
1. 整个生命周期（从onCreate到onDestroy）都可以实时收到消息
2. 激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息

## 在工程中引用
Via Gradle：

```
implementation 'com.jeremyliao:live-event-bus:1.2.2'
```

## 调用方式
#### 订阅消息
- **observe**
生命周期感知，不需要手动取消订阅

```java
LiveEventBus.get()
    .with("key_name", String.class)
    .observe(this, new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
        }
    });
```
- **observeForever**
需要手动取消订阅

```java
LiveEventBus.get()
    .with("key_name", String.class)
    .observeForever(observer);
```

```java
LiveEventBus.get()
    .with("key_name", String.class)
    .removeObserver(observer);
```

#### 发送消息
- **setValue**
在主线程发送消息
```java
LiveEventBus.get().with("key_name").setValue(value);
```
- **postValue**
在后台线程发送消息，订阅者会在主线程收到消息
```java
LiveEventBus.get().with("key_name").postValue(value);
```
#### Sticky模式
支持在注册订阅者的时候设置Sticky模式，这样订阅者可以接收到订阅之前发送的消息

- **observeSticky**
生命周期感知，不需要手动取消订阅，Sticky模式

```java
LiveEventBus.get()
        .with("sticky_key", String.class)
        .observeSticky(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s){
            }
        });
```
- **observeStickyForever**
需要手动取消订阅，Sticky模式

```java
LiveEventBus.get()
        .with("sticky_key", String.class)
        .observeStickyForever(observer);
```

```java
LiveEventBus.get()
        .with("sticky_key", String.class)
        .removeObserver(observer);
```

#### 设置LifecycleObserver接收消息的模式
- [x] 整个生命周期（从onCreate到onDestroy）都可以实时收到消息（LiveEventBus的默认设置）

```java
LiveEventBus.get().lifecycleObserverAlwaysActive(true);
```

- [x] 激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息

```java
LiveEventBus.get().lifecycleObserverAlwaysActive(false);
```

## 混淆规则

```
-dontwarn android.arch.lifecycle.LiveData
-keep class android.arch.lifecycle.LiveData { *; }
-keep class android.arch.lifecycle.LifecycleRegistry { *; }
-keep class android.arch.core.internal.SafeIterableMap { *; }
```

## 其他版本
#### [classic](/branchs/live-event-bus-classic/liveeventbus-classic/src/main/java/com/jeremyliao/liveeventbus/LiveEventBus.java)
- [x] 经典实现版，整个实现就一个java文件
- [x] 只支持激活状态（Started）可以实时收到消息，非激活状态（Stoped）无法实时收到消息，需等到Activity重新变成激活状态，方可收到消息

#### [v2](/branchs/live-event-bus-v2/liveeventbus-v2/src/main/java/com/jeremyliao/liveeventbus)
- [x] v2版，所有特性同master版保持一致
- [x] 为了解决非激活态不能实时收到消息的问题，采用修改LiveData源码的方式实现，master版采用继承LiveData的方式实现

## 示例和DEMO
- [x] 发送、接收消息
- [x] Sticky模式

![基本功能](/images/img1.gif)
![sticky](/images/img2.gif)

- [x] 任何时候都可以订阅消息
- [x] 一个简单的应用场景，发消息关闭所有Activity

![register](/images/img3.gif)
![close all](/images/img4.gif)

- [x] 快速postValue也不会丢失消息
- [x] 整个生命周期（从onCreate到onDestroy）都可以实时收到消息

![postvalue](/images/img5.gif)
![always](/images/img6.gif)

## 文档
#### LiveEventBus实现原理
LiveEventBus的实现原理可参见作者在美团技术博客上的博文：
[Android消息总线的演进之路：用LiveDataBus替代RxBus、EventBus](https://tech.meituan.com/Android_LiveDataBus.html)

## 质量
- [x] 编写了14个测试用例以确保LiveEventBus能够正常运行。
- [x] 具体测试用例参见[LiveEventBusTest](/live-event-bus/app/src/androidTest/java/com/jeremyliao/lebapp/LiveEventBusTest.java)

## 主要功能Commit记录
1. 主要功能完成（Jul 11, 2018）
2. 支持Sticky（Aug 8, 2018）
3. 修复在后台线程PostValue会丢失消息的问题（Aug 9, 2018）
4. 解决发送给Stop状态Observer消息无法及时收到的问题（Aug 18, 2018）
5. 解决了Resumed状态的Activity发生订阅，订阅者会收到订阅之前发布的消息的问题。特别感谢@MelonWXD发现了这个问题（Dec 8，2018）
6. 在removeObserver的时候，检查livedata上有没有observer，没有则删除这个livadata，以减少内存占用。特别感谢@GreenhairTurtle提供的解决方案（Dec 27，2018）
7. 支持设置LifecycleObserver接收消息的模式，支持在整个生命周期实时接收消息和只在激活态实时接收消息两种模式（Jan 22，2019）

## 其他
- 欢迎提Issue与作者交流
- 欢迎提Pull request，帮助 fix bug，增加新的feature，让LiveEventBus变得更强大、更好用

## More Open Source by JeremyLiao

1. [FastSharedPreferences](https://github.com/JeremyLiao/FastSharedPreferences) 一个Android平台的高性能key-value组件
2. [tensorflow-lite-sdk](https://github.com/JeremyLiao/tensorflow-lite-sdk) 一个更加通用的Tensorflow-Lite Android SDK
3. [android-modular](https://github.com/JeremyLiao/android-modular) 一个组件化实施方案的Demo
4. [MessageBus](https://github.com/JeremyLiao/MessageBus) 一个android平台的基于订阅-发布模式的消息框架，支持跨进程消息通信
5. [persistence](https://github.com/JeremyLiao/persistence) 一个android平台的key-value storage framework
6. [LightRxAndroid](https://github.com/JeremyLiao/LightRxAndroid) 另辟蹊径，利用Android Handler实现了一个类似RxJava的链式框架
7. [rxjava-retry](https://github.com/JeremyLiao/rxjava-retry) 封装了几个处理RxJava Retry操作的类
8. [retrofit-mock](https://github.com/JeremyLiao/retrofit-mock) 一个用于Retrofit mock response数据的工具
9. [jacoco-android-demo](https://github.com/JeremyLiao/jacoco-android-demo)  AndroidStudio运行jacoco计算测试覆盖率的Demo
10. [android-gradle-study](https://github.com/JeremyLiao/android-gradle-study) 深入浅出Android Gradle