# Manual Code Optimization
While building a hash implementation for a custom character buffer, we noticed that a manual optimization of the code to utilize CPU pipelines and execution units better surprisingly worked. 

As a side note, this is based on the traditional hash implementation for Strings in the JDK because we assumed that multiplication by 31 is automatically replaced by the compiler with a shift and subtraction. So we started rather with a test if this optimization is done and by accident discovered that manual code reshaping works.

## String Hashing
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);
    hash = 31 * hash + c;
}
```

## Shifting (which will be done automatically)
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);
    hash = (hash << 5) + c - hash;
}
``` 

## Improved by Hinting Independent Operations
```java
for (int i = 0; i < s.length(); i++) 
{
    final char c = s.charAt(i);

    int h1 = hash << 5; // * 32
    int h2 = c - hash; // - 1 * hash aka the before was * 31
    hash = h1 + h2;
}
```

## Further Investigations

You will find two implementations of the shifting because after the strange "hinting" success, we just change the order of the parameters of the formula and had the same success. So, it is not that our manual hinting is the solution, it seems rather a compiler oddity that prevents faster code to be created.

### Slow

```java
hash = (hash << 5) + c - hash;
```

### Faster

```java
hash = (c - hash) + (hash << 5);
```

## Test Results

### Thinkpad T14s AMD


### 