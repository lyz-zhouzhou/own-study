package org.example.other.demo;

public class TestInner {
    public static void main(String[] args) {
        // 尝试创建 Sub 的实例并调用其 method 方法
        Outer.Inner in = new Sub();
        in.method();
    }
}

/**
 * 隐式引用的机制
 * 当创建一个非静态内部类的实例时，Java 编译器会在内部类中生成一个指向其外部类实例的隐藏字段。这个字段通常被称为 this$0，它保存了创建该内部类实例时所关联的外部类实例。
 */
class Outer {
    // 定义一个抽象内部类 Inner
    abstract class Inner {
        // 定义一个抽象方法 method，需要在子类中实现
        abstract void method();

        Inner() {
            // 隐式地持有 this$0 引用 -> 即对外部类实例的引用
        }
    }
}

// Sub 类继承自 Outer 内部的非静态抽象类 Inner
class Sub extends Outer.Inner {

    // 定义一个静态成员 outer，用于保存 Outer 的实例
    static Outer outer = new Outer();//类初始化，先于实例初始化 -> 即在创建Sub对象前，这段代码已执行完成了，且拿到了Outer的实例的引用

    // Sub 的构造器
    Sub() {
        /**
         * 为什么子类也需要这个引用？
         * 当你创建一个继承自非静态内部类的子类时，子类本质上也是非静态内部类的一部分。因此，子类的构造器必须确保这个隐式引用被正确初始化。具体来说，子类的构造器需要调用父类的构造器，并传递给它一个有效的 Outer 实例。
         */
        outer.super();
    }

    // 实现从 Outer.Inner 继承来的抽象方法 method
    @Override
    void method() {
        System.out.println("Hello Inner");
    }
}