# Andulir

一款极简,协同的接口自动化测试工具,面向 `SpringBoot` 和 `Spring Cloud` ,为小型开发团队和个人开发者准备。

通过`Andulir`,你可以:
- 方便的使用注解标记你想要进行测试的接口
- 在本地启动项目后使用其自动的生成数据进行测试
- 在控制台上看到相应的测试结果。

`Andulir`将会通过一个  `atest.xml` 管理所有的测试用例,如果你对测试的结果不太满意,可以在文件中对其进行修改,再修改后再次运行项目时仍然会对之前的用例进行测试.

当对一个接口测试完成之后，你可以修改其在 `xml` 中的 `tag`，让框架忽略此用例，还可以将这个文件在 `git` 等版本管理工具中与源代码文件一同管理，以便于与你的开发伙伴，甚至是前端的对接人员很方便的共享这些接口的测试用例，大大减少开发中测试和沟通的成本。

当项目发布到生产服务器之前，将会对所有开发人员共同管理的所有用例进行统一测试，从而构成CI/CD中的重要一环。

# 1.工作流程：

## 1.1 引入：

在项目根目录内，进行拉取：

```bash
# 原仓库
git clone https://github.com/bearslyricattack/Andulir.git
# Donnie 的fork仓库
git clone https://github.com/Donnie518/Andulir.git
```

IDEA里设为模块的方式：
1. File -> New -> Module From Existing Souces
2. 然后在Select File or Dictionary to import 窗口 **选择 `andulir.iml` 文件（很重要）**
3. 刷新 Maven 导入 Andulir 需要的依赖。（这里 Andulir 应该和其他项目是并列关系）

最后，在需要使用的模块里通过 `pom.xml`导入：

```xml
<dependency>
    <groupId>org.andulir</groupId>
    <artifactId>andulir</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 1.2 配置：

Andulir主打的就是一个极简测试，所以我们尽量把所需的配置降到最低，唯一需要配置的地方是在项目的 `application.yml` ,配置项目位置及 `controller`包所在的位置，从而确定接口扫描的范围：

```yaml
andulir:
  basePackage: com.islet.example
  baseControllerPackage: com.islet.example.controller
```

## 1.3 使用:

### 1.3.1 增加注解:

整个工具只需要一个`@ATest`注解便可完成所有的功能,你可以使用此注解在 `Controller` 层接口方法上标记你希望测试的方法,并为其设置用例的个数:

```java
    @ApiOperation(value = "新增/编辑应急处置单位")
    @PostMapping("/updateEmergencyResponseUnit")
    @ATest(value = 3)
    public BaseResponse<String> updateEmergencyResponseUnit(@RequestBody UpdateEmergencyResponseUnitRequest updateEmergencyResponseUnitRequest){
        return emergencyResponseService.updateEmergencyResponseUnit(updateEmergencyResponseUnitRequest);
    }
```

### 1.3.2 启动项目

当对接口进行足够的标记后,便可以启动 `Andulir`。

启动方法：在main方法里直接调用 `AndulirApplication.start()` 方法。

```java
package com.islet.example;

import org.andulir.AndulirApplication;

public class ExampleControllerTest {
    public static void main(String[] args) {
        AndulirApplication.start(args);
    }
}
```

### 1.3.3 atest.xml

启动后,你就可以在项目的目录下找到一个名为 `atest.xml` 的文件.(如果不存在会自动生成),并会根据带注解的方法的相关信息生成如下格式的xml文件:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<aTestEntity>
    <controllermapping>
        <methodmapping>
            <examplemapping>
                <parametermapping>
                    <type>java.lang.Integer</type>
                    <value>-414655465</value>
                </parametermapping>
            </examplemapping>
            <isRequest>不是请求类</isRequest>
            <name>query</name>
            <status>0</status>
        </methodmapping>
        <name>com.elevator.unit.controller.InspectionItemController</name>
    </controllermapping>
</aTestEntity>
```

- `atestentity`是xml文件的主标签
- `controllermapping`表征方法属于哪个Controler类,name为类的名称(全限定名)
- `methodmapping` 表征方法的具体信息,name是方法的名称,status是方法的状态,方法状态借鉴了git中分支合并的相关概念,当方法首次生成时,status为0(未关闭),当方法调试完成之后,手动的设置方法的status为1(已关闭)
- `examplemapping`表征用例的状态,此标签的个数取决于注解中的value属性.
- `parametermapping` 表征参数的状态,type为参数的类型(全限定名),value为参数的值.第一次生成的时候其值为根据其类型自动生成的随机数据.

当项目第一次启动的时候,不仅仅会生成文件,还会对status为0的方法进行一次测试,并把相关的测试结果输出到控制台:
![QQ截图20231106110500](http://bearsblog.oss-cn-beijing.aliyuncs.com/img/QQ截图20231106110500.png)

### 1.3.4 修改用例:

当你看到自己新编写或者修改的接口的第一次测试数据后,会出现两种情况:

1.随机生成的测试数据没有问题,那么可以直接修改status为1,此接口测试完成

2.随机生成的测试数据存在问题,那么你需要修改xml文件中对应的用例的数据,然后再次启动项目,这样Andulir会根据新的用例再次测试,并同样输出结果,如此反复,只到你认为这个接口没有问题,那么就将status修改为1,测试完成.

### 1.3.5 git同步:

将atest.xml这个文件与其他的文件一起,在git等工具中进行统一的管理.

# 2.架构设计:

Andulir由三部分组成:数据解析器,数据生成器,数据测试器,这三部分通过atest.xml文件为媒介,进行一系列的工作.

## 2.1 数据解析器 dataparser:

数据解析器主要负责解析添加了注解的接口,并生成对应的xml标签,存入文件.

主要是通过JAXBContext,通过把xml映射成实体类的方式处理xml文件.

## 2.2 数据生成器 datagenerator:

数据生成器的主要作用是根据解析器解析出来的类型,自动的生成符合格式的数据.

生成数据的主要方式是通过全限定名类型的匹配与反射.并能支持生成请求类和list的数据.

## 2.3 数据测试器 dataaccess:

数据测试器的主要作用是通过解析xml文件,生成对应的方法然后进行测试.

一般测试接口的方式是通过http的形式进行测试,但是考虑到这个工具的主要应用场景是在本地测试,所以另辟蹊径,不使用http的方式进行测试,而是直接访问接口之下的方法.

具体实现时,因为使用反射生成的controller对象无法注入,所以使用根据名称(applicationcontext)获取bean的形式,获取到原本存在的controller-bean从而进行测试.

至于项目的自启动运行,是通过实现CommandLineRunner,并覆写run方法,从而做到在所有的bean都生成之后再开始操作.

# 3.拓展:

## 3.1 git自定位:

原本设想git不仅仅只能管理atest.xml中的测试用例,而是能通过 git diff ,通过git commit之间的差异寻找有所修改的接口,并持续自动化测试的流程.那样会比使用注解更加方便,更加自动化.

但是当使用git获取到有所修改的文件之后,定位到这些修改需要解析所有源代码的接口并建立和维护索引,尝试之后发现难度略大,于是暂时搁置.

后续可以探索使用 java parser等相关类实现此功能的路径.

## 3.2 请求类测试（接口的参数解析优化）:

目前对请求类和List<>的适配不够完善,只有在parser和 generator中实现此功能,而测试时对其支持还不够完善.

当前的项目在需要测试的接口的参数过于复杂的时候（比如出现请求类中嵌套list，list的对象又是复杂的请求类）会出现无法正确的解析接口的参数，导致自动化测试无法进行的情况出现。（预估此项改进会相对比较复杂）

解决这个问题，可选的思路是参考open-feign的源码中关于参数解析的部分，重新设计新的实现。

## 3.3 引入方式:

目前的引入下载源代码模块 + 依赖引入，后续考虑上传 Maven 相关服务器，继续简化引入流程。

## 3.4 bug修复以及健壮性的提高：

目前的程序完全没有健壮性，如果在运行过程出现什么问题会直接报错并退出。

尝试为程序增加异常处理，容错机制等功能。

## 3.5 自动化测试：

结合jenkins或者github runner 实现在自动化部署之前进行指定或者全部接口的测试，实现更为完整的CI/CD流程。

## 3.6 mock能力：

现在的项目完全没有mock其他诸如中间件等外部依赖的能力，尝试增加以进一步简化接口测试。

# 4.写在最后:

这是笔者设计编写的第一个开源项目,来源只是平常开发中的一些简单的想法.

日常生活和代码开发中,总会有很多很多的想法出现,可能有些比较复杂有探索深度,但是更多的可能只是crud的排列组合,唯一的亮点就是本身的场景有心意,令人眼前一亮.

创意的产生和设计是简单的,或者说是可遇不可求的,而真实的代码开发则是无趣的,费力的,尤其是对软件开发的入门玩家,爱好者,以及为爱发电的小型开发团队而言.

所以产生了Andulir这款工具,或许它现在还存在这样那样的诸多bug,但是它设计的初衷就是为了更够尽自己所能的便利这些人的开发.最终目的是让个人开发者和小型开发团队也能简单的搭建出完成的Devops流程,从而有更多的机会和勇气去开发自己心中所想的软件,或自己,或与朋友一同.

在这个所有人手机中的软件,电脑上的游戏,浏览的网站大同小异的时代,我们总希望,更有创意的设计不仅仅止于设计,而能够变成完整而伟大的作品出现在我们眼前,我们总希望,能进行软件开发的绝对不仅仅只是专业的软件开发者和管理严格的软件开发团队,而是日常生活中每一个有想法的普通人或者伙伴,我们总希望,这个世界上能多一些创意,多一些设计,多一些包容,多一些震撼,点亮人心的作品.

如果你也想参与这个开源项目,请通过qq联系我:2669184984@qq.com 
