package net.sf.openrocket.logging;

import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.motor.Motor;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.simulation.FlightEvent;
import net.sf.openrocket.unit.UnitGroup;

/**
 * A warning message wrapper.
 */
public abstract class Warning extends Message {
	
	/** support to multiple languages warning */
	private static final Translator trans = Application.getTranslator();

	/**
	 * @return a Warning with the specific text.
	 */
	public static Warning fromString(String text) {
		return new Warning.Other(text);
	}


	
	/////////////  Specific warning classes  /////////////
	
	
	/**
	 * A <code>Warning</code> indicating a large angle of attack was encountered.
	 * 
	 * @author Sampo Niskanen <sampo.niskanen@iki.fi>
	 */
	public static class LargeAOA extends Warning {
		private final double aoa;
		
		/**
		 * Sole constructor.  The argument is the AOA that caused this warning.
		 * 
		 * @param aoa  the angle of attack that caused this warning
		 */
		public LargeAOA(double aoa) {
			this.aoa = aoa;
		}
		
		@Override
		public String toString() {
			if (Double.isNaN(aoa))
				//// Large angle of attack encountered.
				return trans.get("Warning.LargeAOA.str1");
			//// Large angle of attack encountered (
			return (trans.get("Warning.LargeAOA.str2") +
					UnitGroup.UNITS_ANGLE.getDefaultUnit().toString(aoa) + ").");
		}

		@Override
		public boolean replaceBy(Message other) {
			if (!(other instanceof LargeAOA))
				return false;
			
			LargeAOA o = (LargeAOA) other;
			if (Double.isNaN(this.aoa)) // If this has value NaN then replace
				return true;
			return (o.aoa > this.aoa);
		}
	}
	
	/**
	 * A <code>Warning</code> indicating recovery device deployment at high speed was encountered.
	 * 
	 * @author Craig Earls <enderw88@gmail.com>
	 */
	public static class HighSpeedDeployment extends Warning {
		private final double recoverySpeed;
		
		/**
		 * Sole constructor.  The argument is the speed that caused this warning.
		 * 
		 * @param speed  the speed that caused this warning
		 */
		public HighSpeedDeployment(double speed) {
			this.recoverySpeed = speed;
		}
		
		@Override
		public String toString() {
			if (Double.isNaN(recoverySpeed)) {
				return trans.get("Warning.RECOVERY_HIGH_SPEED");
			}
			return trans.get("Warning.RECOVERY_HIGH_SPEED") + " (" + UnitGroup.UNITS_VELOCITY.toStringUnit(recoverySpeed) + ")";
		}
		
		@Override
		public boolean replaceBy(Message other) {
			return false;
		}
	}

	/**
	 * A <code>Warning</code> indicating flight events occurred after ground hit
	 *
	 */
	public static class EventAfterLanding extends Warning {
		private final FlightEvent event;
		
		/**
		 * Sole constructor.  The argument is an event which has occurred after landing
		 *
		 * @param _event the event that caused this warning
		 */
		public EventAfterLanding(FlightEvent _event)  {
			this.event = _event;
		}

		// I want a warning on every event that occurs after we land,
		// so severity of problem is clear to the user
		@Override
		public boolean equals(Object o) {
			return false;
		}
		

		@Override
		public String toString() {
			return trans.get("Warning.EVENT_AFTER_LANDING") + event.getType();
		}

		@Override
		public boolean replaceBy(Message other) {
			return false;
		}
	}
	
	public static class MissingMotor extends Warning {
		
		private Motor.Type type = null;
		private String manufacturer = null;
		private String designation = null;
		private String digest = null;
		private double diameter = Double.NaN;
		private double length = Double.NaN;
		private double delay = Double.NaN;
		
		@Override
		public String toString() {
			String str = "No motor with designation '" + designation + "'";
			if (manufacturer != null)
				str += " for manufacturer '" + manufacturer + "'";
			str += " found.";
			return str;
		}
		
		public Motor.Type getType() {
			return type;
		}
		
		
		public void setType(Motor.Type type) {
			this.type = type;
		}
		
		
		public String getManufacturer() {
			return manufacturer;
		}
		
		
		public void setManufacturer(String manufacturer) {
			this.manufacturer = manufacturer;
		}
		
		
		public String getDesignation() {
			return designation;
		}
		
		
		public void setDesignation(String designation) {
			this.designation = designation;
		}
		
		
		public String getDigest() {
			return digest;
		}
		
		
		public void setDigest(String digest) {
			this.digest = digest;
		}
		
		
		public double getDiameter() {
			return diameter;
		}
		
		
		public void setDiameter(double diameter) {
			this.diameter = diameter;
		}
		
		
		public double getLength() {
			return length;
		}
		
		
		public void setLength(double length) {
			this.length = length;
		}
		
		
		public double getDelay() {
			return delay;
		}
		
		
		public void setDelay(double delay) {
			this.delay = delay;
		}
		
		
		@Override
		public boolean replaceBy(Message other) {
			return false;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			long temp;
			temp = Double.doubleToLongBits(delay);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((designation == null) ? 0 : designation.hashCode());
			temp = Double.doubleToLongBits(diameter);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((digest == null) ? 0 : digest.hashCode());
			temp = Double.doubleToLongBits(length);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((manufacturer == null) ? 0 : manufacturer.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			MissingMotor other = (MissingMotor) obj;
			if (Double.doubleToLongBits(delay) != Double
					.doubleToLongBits(other.delay))
				return false;
			if (designation == null) {
				if (other.designation != null)
					return false;
			} else if (!designation.equals(other.designation))
				return false;
			if (Double.doubleToLongBits(diameter) != Double
					.doubleToLongBits(other.diameter))
				return false;
			if (digest == null) {
				if (other.digest != null)
					return false;
			} else if (!digest.equals(other.digest))
				return false;
			if (Double.doubleToLongBits(length) != Double
					.doubleToLongBits(other.length))
				return false;
			if (manufacturer == null) {
				if (other.manufacturer != null)
					return false;
			} else if (!manufacturer.equals(other.manufacturer))
				return false;
			if (type != other.type)
				return false;
			return true;
		}
		
	}
	
	
	/**
	 * An unspecified warning type.  This warning type holds a <code>String</code>
	 * describing it.  Two warnings of this type are considered equal if the strings
	 * are identical.
	 * 
	 * @author Sampo Niskanen <sampo.niskanen@iki.fi>
	 */
	public static class Other extends Warning {
		private final String description;
		
		public Other(String description) {
			this.description = description;
		}
		
		@Override
		public String toString() {
			return description;
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Other))
				return false;
			
			Other o = (Other) other;
			return (o.description.equals(this.description));
		}
		
		@Override
		public int hashCode() {
			return description.hashCode();
		}
		
		@Override
		public boolean replaceBy(Message other) {
			return false;
		}
	}
	
	
	/** A <code>Warning</code> that the body diameter is discontinuous. */
	////Discontinuity in rocket body diameter.
	public static final Warning DIAMETER_DISCONTINUITY = new Other(trans.get("Warning.DISCONTINUITY"));

	/** A <code>Warning</code> that a ComponentAssembly has an open forward end */	
	public static final Warning OPEN_AIRFRAME_FORWARD = new Other(trans.get("Warning.OPEN_AIRFRAME_FORWARD"));

	/** A <code>Warning</code> that there is a gap in the airframe */
	public static final Warning AIRFRAME_GAP = new Other(trans.get("Warning.AIRFRAME_GAP"));

	/** A <code>Warning</code> that there are overlapping airframe components */
	public static final Warning AIRFRAME_OVERLAP = new Other(trans.get("Warning.AIRFRAME_OVERLAP"));

	/** A <code>Warning</code> that an inline podset is completely forward of its parent component */
	public static final Warning PODSET_FORWARD = new Other(trans.get("Warning.PODSET_FORWARD"));

	/** A <code>Warning</code> that an inline podset overlaps its parent component */
	public static final Warning PODSET_OVERLAP = new Other(trans.get("Warning.PODSET_OVERLAP"));
	
	/** A <code>Warning</code> that the fins are thick compared to the rocket body. */
	////Thick fins may not be modeled accurately.
	public static final Warning THICK_FIN = new Other(trans.get("Warning.THICK_FIN"));
	
	/** A <code>Warning</code> that the fins have jagged edges. */
	////Jagged-edged fin predictions may be inaccurate.
	public static final Warning JAGGED_EDGED_FIN = new Other(trans.get("Warning.JAGGED_EDGED_FIN"));
	
	/** A <code>Warning</code> that the fins have a zero area. */
	////Fins with no area will not affect aerodynamics
	public static final Warning ZERO_AREA_FIN = new Other(trans.get("Warning.ZERO_AREA_FIN"));
	
	/** A <code>Warning</code> that simulation listeners have affected the simulation */
	////Listeners modified the flight simulation
	public static final Warning LISTENERS_AFFECTED = new Other(trans.get("Warning.LISTENERS_AFFECTED"));
	
	////Recovery device opened while motor still burning.
	public static final Warning RECOVERY_DEPLOYMENT_WHILE_BURNING = new Other(trans.get("Warning.RECOVERY_DEPLOYMENT_WHILE_BURNING"));
	
	////No recovery device for simulation
	public static final Warning NO_RECOVERY_DEVICE = new Other(trans.get("Warning.NO_RECOVERY_DEVICE"));
	
	//// Invalid parameter encountered, ignoring.
	public static final Warning FILE_INVALID_PARAMETER = new Other(trans.get("Warning.FILE_INVALID_PARAMETER"));
	
	public static final Warning PARALLEL_FINS = new Other(trans.get("Warning.PARALLEL_FINS"));

	public static final Warning SUPERSONIC = new Other(trans.get("Warning.SUPERSONIC"));
	
	public static final Warning RECOVERY_LAUNCH_ROD = new Other(trans.get("Warning.RECOVERY_LAUNCH_ROD"));
	
	public static final Warning TUMBLE_UNDER_THRUST = new Other(trans.get("Warning.TUMBLE_UNDER_THRUST"));

	public static final Warning EVENT_AFTER_LANDING = new Other(trans.get("Warning.EVENT_AFTER_LANDING"));

	public static final Warning ZERO_VOLUME_BODY = new Other(trans.get("Warning.ZERO_VOLUME_BODY"));

	public static final Warning TUBE_SEPARATION = new Other(trans.get("Warning.TUBE_SEPARATION"));
	public static final Warning TUBE_OVERLAP = new Other(trans.get("Warning.TUBE_OVERLAP"));

	/** A <code>Warning</code> that stage separation occurred at other than the last stage */
	public static final Warning SEPARATION_ORDER = new Other(trans.get("Warning.SEPARATION_ORDER"));

	/** A <code>Warning</code> that stage separation occurred before the rocket cleared the launch rod or rail */
	public static final Warning EARLY_SEPARATION = new Other(trans.get("Warning.EARLY_SEPARATION"));

	public static final Warning EMPTY_BRANCH = new Other(trans.get("Warning.EMPTY_BRANCH"));
}
