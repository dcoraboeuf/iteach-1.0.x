package net.nemerosa.iteach.common;

import org.junit.Test;

import static org.junit.Assert.*;

public class IDTest {

	@Test
	public void success() {
		ID id = ID.success(10);
		assertTrue(id.isSuccess());
		assertEquals(10, id.getValue());
	}

	@Test
	public void failure() {
		ID id = ID.failure();
		assertFalse(id.isSuccess());
		assertEquals(-1, id.getValue());
	}
	
	@Test
	public void count_not1_with_id () {
		ID id = ID.count(0).withId(10);
		assertFalse(id.isSuccess());
		assertEquals(-1, id.getValue());
	}
	
	@Test
	public void count_1_with_id () {
		ID id = ID.count(1).withId(10);
		assertTrue(id.isSuccess());
		assertEquals(10, id.getValue());
	}

}
