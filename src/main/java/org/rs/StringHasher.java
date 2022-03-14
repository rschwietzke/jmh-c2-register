package org.rs;

import org.junit.Assert;
import org.junit.Test;

public class StringHasher
{
    public static int hashShiftingSplit(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);
            
            int h1 = hash << 5;
            int h2 = c - hash;
            hash = h1 + h2;
        }

        return hash;
    }   

    public static int hashShiftingOneLine(final CharSequence s)
    {
        int hash = 0;

        for (int i = 0; i < s.length(); i++) 
        {
            final char c = s.charAt(i);
            hash = (hash << 5) + c - hash;
        }

        return hash;
    } 

    @Test
    public void test()
    {
        {
            var s = "";
            Assert.assertEquals(s.hashCode(), hashShiftingSplit(s));
            Assert.assertEquals(s.hashCode(), hashShiftingOneLine(s));
        }
        {
            var s = "foo";
            Assert.assertEquals(s.hashCode(), hashShiftingSplit(s));
            Assert.assertEquals(s.hashCode(), hashShiftingOneLine(s));
        }
        {
            var s = "9823  a987s8 as8df asd7f 8asjwhjhjh sshd f98s893q8ukjkhkjahkj haosu au09sd093093pqpj";
            Assert.assertEquals(s.hashCode(), hashShiftingSplit(s));
            Assert.assertEquals(s.hashCode(), hashShiftingOneLine(s));
        }

    }

}
