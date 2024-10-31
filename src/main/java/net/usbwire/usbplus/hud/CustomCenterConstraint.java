package net.usbwire.usbplus.hud;

import gg.essential.elementa.UIComponent;
import gg.essential.elementa.constraints.ConstraintType;
import gg.essential.elementa.constraints.PositionConstraint;
import gg.essential.elementa.constraints.resolution.ConstraintVisitor;
import gg.essential.elementa.state.BasicState;
import gg.essential.elementa.state.MappedState;
import gg.essential.elementa.state.State;
import gg.essential.elementa.utils.ExtensionsKt;

/**
 * Modifed version of CenterConstraint and RelativeConstraint that allows for adjustment Parameter
 * should between 0.0f and 1.0f
 * Couldn't figure out a better way...
 */
public class CustomCenterConstraint implements PositionConstraint {
	private float cachedValue = 0f;
	private boolean recalculate = true;
	private UIComponent constrainTo = null;
	private final MappedState<Float, Float> valueState;

	public CustomCenterConstraint(State<Float> value) {
		this.valueState = new MappedState<>(value, v -> v);
	}

	public CustomCenterConstraint(float value) {
		this(new BasicState<>(value));
	}

	public float getValue() {
		return valueState.get();
	}

	public void setValue(float value) {
		valueState.set(value);
	}

	public CustomCenterConstraint bindValue(State<Float> newState) {
		valueState.rebind(newState);
		return this;
	}

	@Override
	public float getXPositionImpl(UIComponent component) {
		UIComponent parent = constrainTo != null ? constrainTo : component.getParent();
		if (component.isPositionCenter()) {
			return parent.getLeft()
					+ ExtensionsKt.roundToRealPixels(parent.getWidth() * valueState.get());
		} else {
			return parent.getLeft() + ExtensionsKt.roundToRealPixels(
					parent.getWidth() * valueState.get() - component.getWidth() * valueState.get());
		}
	}

	@Override
	public float getYPositionImpl(UIComponent component) {
		UIComponent parent = constrainTo != null ? constrainTo : component.getParent();
		if (component.isPositionCenter()) {
			return parent.getTop()
					+ ExtensionsKt.roundToRealPixels(parent.getHeight() * valueState.get());
		} else {
			return parent.getTop() + ExtensionsKt.roundToRealPixels(
					parent.getHeight() * valueState.get() - component.getHeight() * valueState.get());
		}
	}

	// @Override
	// public float getWidthImpl(UIComponent component) {
	// 	return
	// }

	// @Override
	// public float getHeightImpl(UIComponent component) {
	// 	return 0f;
	// }

	// @Override
	// public float getRadiusImpl(UIComponent component) {
	// 	return 0f;
	// }

	@Override
	public boolean getRecalculate() {
		return this.recalculate;
	}

	@Override
	public void setRecalculate(boolean recalculate) {
		this.recalculate = recalculate;
	}

	@Override
	public Float getCachedValue() {
		return this.cachedValue;
	}

	@Override
	public void setCachedValue(Float value) {
		this.cachedValue = value;
	}

	@Override
	public void setConstrainTo(UIComponent component) {
		this.constrainTo = component;
	}

	public UIComponent getConstrainTo() {
		return constrainTo;
	}

	@Override
	public void visitImpl(ConstraintVisitor visitor, ConstraintType type) {
		switch (type) {
			case X:
				visitor.visitParent(ConstraintType.X);
				visitor.visitParent(ConstraintType.WIDTH);
				if (!visitor.getComponent().isPositionCenter())
					visitor.visitSelf(ConstraintType.WIDTH);
				break;
			case Y:
				visitor.visitParent(ConstraintType.Y);
				visitor.visitParent(ConstraintType.HEIGHT);
				if (!visitor.getComponent().isPositionCenter())
					visitor.visitSelf(ConstraintType.HEIGHT);
				break;
			default:
				throw new IllegalArgumentException(type.getPrettyName());
		}
	}
}
