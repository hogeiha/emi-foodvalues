# EMI Food Values

一个 EMI 附属模组，给食物加两个能看数值的类别：饥饿值和实际饱和度一眼可见，并且按数值排好序。

- Minecraft 1.19.2 / Forge 43.x / Java 17
- 客户端模组，服务端不需要装
- 需要 EMI（Forge 1.19.2 的 1.1.x，开发时用的是 `1.1.24+1.19.2`）

## 内容

| 类别 | 排序 |
| --- | --- |
| 食物数值（按饱和度） | 实际饱和度从高到低 |
| 食物数值（按饥饿值） | 饥饿值从高到低 |

每个条目显示物品、名字、`饥饿值：x`、`饱和度：y`。
饱和度用原版公式 `nutrition * saturationModifier * 2`，也就是吃下去实际恢复的量。

另外给每个食物注册了搜索别名，在 EMI 搜索栏输入 `食物`（英文语言 `food`）就能把能吃的全部筛出来。

## 用法

- 搜 `食物`，或者叠加关键词，例如 `食物 金`（空格在 EMI 里是 AND）。
- 鼠标停在食物上按 `U` 查看用途，列表里能看到属于这两个类别的条目。
- 按 `R` 打开配方页后，顶栏的类别栏里可以找到这两个类别，进去就是排好序的列表。

## 安装

把 jar 放进 `mods` 即可。启动后日志里会有类似的一行：

```
[emifoodvalues/]: Registered N food entries in 2 EMI categories
```

`N` 是当前整合包里食物数量 × 2。

## 编译

Java 17 + Gradle 8.1+（仓库自带的 wrapper 是 8.5）。

```
gradlew.bat build     # Windows
./gradlew build       # Linux / macOS
```

产物在 `build/libs/`。发布请用这个 jar，ForgeGradle 打包时已经做过重映射，直接拿 `build/classes` 里的 class 在正式环境跑不起来。

EMI 的 maven（`repo.sleeping.town`）国内偶尔连不上，`build.gradle` 里已经把这个仓库限定为只解析 `dev.emi`，避免拖慢 Forge 依赖；实在连不上可以用 `-PemiLocalRepo=<本地 maven 目录>` 指定一份本地副本。

## 开发备注

- Forge 下 EMI 通过 `@EmiEntrypoint` 注解扫描插件，不需要在 `mods.toml` 里登记。
- 类别名是 `emi.category.<命名空间>.<路径>`，要和语言文件的键对应。
- 排序用 `EmiRecipeCategory` 的第 4 个参数（比较器），EMI 烘焙配方表时会用它排序。
- 搜索别名用 `EmiRegistry#addAlias`。

## 已知限制

- 搜索栏不支持 `饱和度>5` 这类数值表达式，EMI 的搜索解析器不认。想要区间筛选，多加几个固定区间的类别更省事。
- 一个类别里条目比较多，第一次打开会稍微卡一下。

## 许可

MIT
