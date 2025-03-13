# **`peek()` vs. `map()` 在 Java Stream API 中的区别**

在 Java Stream API 中，`peek()` 和 `map()` 主要的区别在于它们的作用和使用场景：

| 方法                  | 主要用途          | 是否改变数据           | 作用                                      |
|----------------------|-----------------|----------------------|-----------------------------------------|
| `peek(Consumer<T>)` | **调试/副作用操作** | ❌ 不会改变流中的元素  | 适用于日志、调试、数据收集，不影响数据流   |
| `map(Function<T, R>)` | **数据转换**     | ✅ 会改变流中的元素    | 适用于数据转换（如字符串转换、对象转换） |

---

## **1. `peek()`**
- `peek()` 主要用于 **调试** 或 **执行副作用**（如打印日志、计数）。
- **不会改变流中的元素**，只是“偷偷地”看一眼并执行 `Consumer<T>`。

### **示例：使用 `peek()` 进行调试**
```java
List<String> names = List.of("Alice", "Bob", "Charlie");

names.stream()
    .peek(name -> System.out.println("Before processing: " + name)) // 仅打印，不改变数据
    .map(String::toUpperCase)
    .peek(name -> System.out.println("After processing: " + name))  // 观察转换后的数据
    .forEach(System.out::println);
