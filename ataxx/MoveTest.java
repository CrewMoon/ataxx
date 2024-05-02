package ataxx;

import ataxx.Move;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class MoveTest {


    @Test
    public void testIsCloneTrue(){
        Assert.assertEquals(Boolean.valueOf("true"), Move.isClone("c1","d2"));
        assertEquals(Boolean.valueOf("true"), Move.isClone("b1","b2"));
    }

    @Test
    public void testIsCloneFalse(){
        assertEquals(Boolean.valueOf("false"), Move.isClone("c1","c1"));
        assertEquals(Boolean.valueOf("false"), Move.isClone("c1","c3"));
    }

    @Test
    public void testIsCloneOutOfBoundary(){
        // source out
        assertEquals(Boolean.valueOf("false"), Move.isClone("h1","g1"));
        // dest out
        assertEquals(Boolean.valueOf("false"), Move.isClone("g1","h3"));
        assertEquals(Boolean.valueOf("false"), Move.isClone("c7","c8"));
        // both out
        assertEquals(Boolean.valueOf("false"), Move.isClone("h1","i3"));
    }

    @Test
    public void testIsCloneNull(){
        assertEquals(Boolean.valueOf("false"), Move.isClone(null,"c1"));
        assertEquals(Boolean.valueOf("false"), Move.isClone("c1",null));
        assertEquals(Boolean.valueOf("false"), Move.isClone(null,null));
    }


    @Test
    public void testIsJumpTrue(){
        assertEquals(Boolean.valueOf("true"), Move.isJump("c1","d3"));
        assertEquals(Boolean.valueOf("true"), Move.isJump("c1","c3"));
    }

    @Test
    public void testIsJumpFalse(){
        assertEquals(Boolean.valueOf("false"), Move.isJump("c4","d3"));
        assertEquals(Boolean.valueOf("false"), Move.isJump("b2","b5"));
    }


}
