package net.usbwire.usbplus.hud

import gg.essential.elementa.UIComponent
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.resolution.ConstraintVisitor
import gg.essential.elementa.state.*
import gg.essential.elementa.utils.roundToRealPixels

/**
 * Modifed version of CenterConstraint and RelativeConstraint that allows for adjustment Parameter
 * should between 0.0f and 1.0f
 */
class CustomCenterConstraint constructor(value: State<Float>) : PositionConstraint {
	@JvmOverloads
	constructor(value: Float = 0.5f) : this(BasicState(value))

	override var cachedValue = 0f
	override var recalculate = true
	override var constrainTo: UIComponent? = null

	private val valueState: MappedState<Float, Float> = value.map { it }

	var value: Float
		get() = valueState.get()
		set(value) {
			valueState.set(value)
		}

	fun bindValue(newState: State<Float>) = apply { valueState.rebind(newState) }

	override fun getXPositionImpl(component: UIComponent): Float {
		val parent = constrainTo ?: component.parent

		return if (component.isPositionCenter()) {
			parent.getLeft() + (parent.getWidth() * valueState.get()).roundToRealPixels()
		} else {
			parent.getLeft() +
				(parent.getWidth() * valueState.get() - component.getWidth() * valueState.get())
					.roundToRealPixels()
		}
	}

	override fun getYPositionImpl(component: UIComponent): Float {
		val parent = constrainTo ?: component.parent

		return if (component.isPositionCenter()) {
			parent.getTop() + (parent.getHeight() * valueState.get()).roundToRealPixels()
		} else {
			parent.getTop() +
				(parent.getHeight() * valueState.get() - component.getHeight() * valueState.get())
					.roundToRealPixels()
		}
	}

	override fun visitImpl(visitor: ConstraintVisitor, type: ConstraintType) {
		when (type) {
			ConstraintType.X -> {
				visitor.visitParent(ConstraintType.X)
				visitor.visitParent(ConstraintType.WIDTH)
				if (!visitor.component.isPositionCenter()) visitor.visitSelf(ConstraintType.WIDTH)
			}

			ConstraintType.Y -> {
				visitor.visitParent(ConstraintType.Y)
				visitor.visitParent(ConstraintType.HEIGHT)
				if (!visitor.component.isPositionCenter()) visitor.visitSelf(ConstraintType.HEIGHT)
			}

			else -> throw IllegalArgumentException(type.prettyName)
		}
	}
}