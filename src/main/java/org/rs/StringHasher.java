package org.rs;

import org.junit.Assert;
import org.junit.Test;

public class StringHasher
{
    public static int original(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);
            hash = 31 * hash + c;
        }
        
        return hash;
    } 
        
    public static int oneLine(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);
            hash = (hash << 5) + c - hash;
        }

        return hash;
    } 

    public static int oneLineReordered(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);
            hash = (c - hash) + (hash << 5);
        }

        return hash;
    } 
    
    public static int decomposed1(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);

            int h1 = hash << 5; // * 32
            int h2 = c - hash; // - 1 * hash aka the before was * 31
            hash = h1 + h2;
        }

        return hash;
    }

    public static int decomposed2(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);

            int h1 = hash << 5; // * 32
            int h2 = c - hash; // - 1 * hash aka the before was * 31
            int h3 = h1 + h2;
            hash = h3;
        }

        return hash;
    }   

    public static int decomposed3(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);

            int h1 = hash << 5;
            int h2 = -hash;
            int h3 = c + h2; 
            int h4 = h1 + h3;
            hash = h4;
        }

        return hash;
    } 
    
    public static int decomposed3Reordered(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            int h1 = hash << 5;
            int h2 = -hash;

            final char c = s.charAt(i);

            int h3 = c + h2; 
            int h4 = h1 + h3;
            hash = h4;
        }

        return hash;
    } 
//    public static int hashShiftingStepped(final CharSequence s)
//    {
//        int hash = 0;
//
//        for (int i = 0; i < s.length(); i++) 
//        {
//            final char c = s.charAt(i);
//
//            int h1 = hash << 5;
//            int h2 = h1 + c;
//            int h3 = h2 - hash;
//            hash = h3;
//        }
//
//        return hash;
//    }  


    @Test
    public void test()
    {
        var t = new Object() 
        {
            public void test(final String s)
            {
                Assert.assertEquals(s.hashCode(), original(s));
                Assert.assertEquals(s.hashCode(), oneLine(s));
                Assert.assertEquals(s.hashCode(), oneLineReordered(s));
                Assert.assertEquals(s.hashCode(), decomposed1(s));
                Assert.assertEquals(s.hashCode(), decomposed2(s));
                Assert.assertEquals(s.hashCode(), decomposed3(s));
                Assert.assertEquals(s.hashCode(), decomposed3Reordered(s));
            }
        };
        
        t.test("");
        t.test("foo");
        t.test("9823  a987s8 as8df asd7f 8asjwhjhjh sshd f98s893q8ukjkhkjahkj haosu au09sd093093pqpj");

    }

}
