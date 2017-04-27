# LoveWeRead

原理: 通过 UiAutomator 进行自动化的翻页，达到自动阅读的效果，从而获取到微信读书的书币.

步骤: 

1. 在微信阅读 (1.5.5版本之前) 中下载在 WeReadingHackTest.java (androidTest 目录中) 中列出的免费书籍; 
```
        mBooks.add("深入分析Java Web技术内幕") ;
        mBooks.add("大学·中庸·尚书·周易") ;
        mBooks.add("人性的弱点") ;
        mBooks.add("瓦尔登湖") ;
        mBooks.add("人间词话") ;
        mBooks.add("傲慢与偏见") ;
        mBooks.add("国学知识大全") ;
```
2. 然后运行 WeReadingHackTest 测试类即可，该测试类会模拟用户翻页300次，也就是模拟阅读5个小时以上
