package io.silverspoon.bulldog.core.mocks;

import io.silverspoon.bulldog.core.gpio.Pin;
import io.silverspoon.bulldog.core.gpio.base.AbstractPinFeature;

public class MockedPinFeature2 extends AbstractPinFeature {
	
	public MockedPinFeature2(Pin pin) {
		super(pin);
	
	}

	@Override
	public String getName() {
		return "Mocked Feature";
	}

	@Override
	protected void setupImpl() {
	}

	@Override
	protected void teardownImpl() {
	}
}

