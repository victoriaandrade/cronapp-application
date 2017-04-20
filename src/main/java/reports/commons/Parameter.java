package reports.commons;

import java.io.Serializable;

import org.apache.xerces.dom.ElementNSImpl;

import reports.utils.Geleia;
import reports.utils.ReportUtils;

public class Parameter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private ParameterType type;

	private Object value;

	public Parameter() {

	}

	public Parameter(String name, ParameterType type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType type) {
		this.type = type;
	}

	public Object getValue() {
		if (this.value == null)
			return null;
		String value;
		if (this.value instanceof ElementNSImpl)
			value = ((ElementNSImpl) this.value).getFirstChild().getNodeValue();
		else
			value = String.valueOf(this.value);
		switch (type) {
		case INTEGER:
			return Geleia.intNotNull(value);
		case DATE:
			return ReportUtils.getOnlyDate(value);
		case DOUBLE:
			return Geleia.doubleNotNull(value);
		case FLOAT:
			return Geleia.floatNotNull(value);
		case BOOLEAN:
			return Geleia.booleanNotNull(value);
		case LONG:
			return Geleia.longNotNull(value);
		default:
			return Geleia.stringNotNull(value);
		}
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Parameter parameter = (Parameter) o;
		if (name != null ? !name.equals(parameter.name) : parameter.name != null)
			return false;
		if (type != null ? !type.equals(parameter.type) : parameter.type != null)
			return false;
		return value != null ? value.equals(parameter.value) : parameter.value == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Parameter{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", value=" + value + '}';
	}

}
