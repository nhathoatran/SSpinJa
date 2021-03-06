/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>String</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see scheduling.dsl.DslPackage#getString()
 * @model
 * @generated
 */
public enum String implements Enumerator
{
  /**
   * The '<em><b>Temp</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TEMP_VALUE
   * @generated
   * @ordered
   */
  TEMP(0, "temp", "int_temp"),

  /**
   * The '<em><b>Byte</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BYTE_VALUE
   * @generated
   * @ordered
   */
  BYTE(1, "byte", "byte"),

  /**
   * The '<em><b>Int</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #INT_VALUE
   * @generated
   * @ordered
   */
  INT(2, "int", "int"),

  /**
   * The '<em><b>Time</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIME_VALUE
   * @generated
   * @ordered
   */
  TIME(3, "time", "time"),

  /**
   * The '<em><b>Bool</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOL_VALUE
   * @generated
   * @ordered
   */
  BOOL(4, "bool", "bool"),

  /**
   * The '<em><b>Clock</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CLOCK_VALUE
   * @generated
   * @ordered
   */
  CLOCK(5, "clock", "clock"),

  /**
   * The '<em><b>Process</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROCESS_VALUE
   * @generated
   * @ordered
   */
  PROCESS(6, "process", "process"),

  /**
   * The '<em><b>Queue</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #QUEUE_VALUE
   * @generated
   * @ordered
   */
  QUEUE(7, "queue", "fifo"),

  /**
   * The '<em><b>Stack</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STACK_VALUE
   * @generated
   * @ordered
   */
  STACK(8, "stack", "lifo"),

  /**
   * The '<em><b>Ndbehavior</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NDBEHAVIOR_VALUE
   * @generated
   * @ordered
   */
  NDBEHAVIOR(9, "ndbehavior", "ND_behavior"),

  /**
   * The '<em><b>Searching</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SEARCHING_VALUE
   * @generated
   * @ordered
   */
  SEARCHING(10, "searching", "Searching"),

  /**
   * The '<em><b>Error</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ERROR_VALUE
   * @generated
   * @ordered
   */
  ERROR(11, "error", "Error"),

  /**
   * The '<em><b>Property</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROPERTY_VALUE
   * @generated
   * @ordered
   */
  PROPERTY(12, "property", "Property"),

  /**
   * The '<em><b>All</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ALL_VALUE
   * @generated
   * @ordered
   */
  ALL(13, "all", "All"),

  /**
   * The '<em><b>Aselect</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ASELECT_VALUE
   * @generated
   * @ordered
   */
  ASELECT(14, "aselect", "<select_process>"),

  /**
   * The '<em><b>Anew</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ANEW_VALUE
   * @generated
   * @ordered
   */
  ANEW(15, "anew", "<new_process>"),

  /**
   * The '<em><b>Aclock</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACLOCK_VALUE
   * @generated
   * @ordered
   */
  ACLOCK(16, "aclock", "<clock>"),

  /**
   * The '<em><b>Apretake</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APRETAKE_VALUE
   * @generated
   * @ordered
   */
  APRETAKE(17, "apretake", "<pre_take>"),

  /**
   * The '<em><b>Aposttake</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APOSTTAKE_VALUE
   * @generated
   * @ordered
   */
  APOSTTAKE(18, "aposttake", "<post_take>"),

  /**
   * The '<em><b>Aschedulingaction</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ASCHEDULINGACTION_VALUE
   * @generated
   * @ordered
   */
  ASCHEDULINGACTION(19, "aschedulingaction", "<scheduling_action>"),

  /**
   * The '<em><b>Aprocessaction</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APROCESSACTION_VALUE
   * @generated
   * @ordered
   */
  APROCESSACTION(20, "aprocessaction", "<process_action>"),

  /**
   * The '<em><b>Select</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SELECT_VALUE
   * @generated
   * @ordered
   */
  SELECT(21, "select", "select_process"),

  /**
   * The '<em><b>New</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NEW_VALUE
   * @generated
   * @ordered
   */
  NEW(22, "new", "new_process"),

  /**
   * The '<em><b>Pretake</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRETAKE_VALUE
   * @generated
   * @ordered
   */
  PRETAKE(23, "pretake", "pre_take"),

  /**
   * The '<em><b>Posttake</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #POSTTAKE_VALUE
   * @generated
   * @ordered
   */
  POSTTAKE(24, "posttake", "post_take");

  /**
   * The '<em><b>Temp</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TEMP
   * @model name="temp" literal="int_temp"
   * @generated
   * @ordered
   */
  public static final int TEMP_VALUE = 0;

  /**
   * The '<em><b>Byte</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BYTE
   * @model name="byte"
   * @generated
   * @ordered
   */
  public static final int BYTE_VALUE = 1;

  /**
   * The '<em><b>Int</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #INT
   * @model name="int"
   * @generated
   * @ordered
   */
  public static final int INT_VALUE = 2;

  /**
   * The '<em><b>Time</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TIME
   * @model name="time"
   * @generated
   * @ordered
   */
  public static final int TIME_VALUE = 3;

  /**
   * The '<em><b>Bool</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOL
   * @model name="bool"
   * @generated
   * @ordered
   */
  public static final int BOOL_VALUE = 4;

  /**
   * The '<em><b>Clock</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CLOCK
   * @model name="clock"
   * @generated
   * @ordered
   */
  public static final int CLOCK_VALUE = 5;

  /**
   * The '<em><b>Process</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROCESS
   * @model name="process"
   * @generated
   * @ordered
   */
  public static final int PROCESS_VALUE = 6;

  /**
   * The '<em><b>Queue</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #QUEUE
   * @model name="queue" literal="fifo"
   * @generated
   * @ordered
   */
  public static final int QUEUE_VALUE = 7;

  /**
   * The '<em><b>Stack</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STACK
   * @model name="stack" literal="lifo"
   * @generated
   * @ordered
   */
  public static final int STACK_VALUE = 8;

  /**
   * The '<em><b>Ndbehavior</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NDBEHAVIOR
   * @model name="ndbehavior" literal="ND_behavior"
   * @generated
   * @ordered
   */
  public static final int NDBEHAVIOR_VALUE = 9;

  /**
   * The '<em><b>Searching</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SEARCHING
   * @model name="searching" literal="Searching"
   * @generated
   * @ordered
   */
  public static final int SEARCHING_VALUE = 10;

  /**
   * The '<em><b>Error</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ERROR
   * @model name="error" literal="Error"
   * @generated
   * @ordered
   */
  public static final int ERROR_VALUE = 11;

  /**
   * The '<em><b>Property</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROPERTY
   * @model name="property" literal="Property"
   * @generated
   * @ordered
   */
  public static final int PROPERTY_VALUE = 12;

  /**
   * The '<em><b>All</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ALL
   * @model name="all" literal="All"
   * @generated
   * @ordered
   */
  public static final int ALL_VALUE = 13;

  /**
   * The '<em><b>Aselect</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ASELECT
   * @model name="aselect" literal="&lt;select_process&gt;"
   * @generated
   * @ordered
   */
  public static final int ASELECT_VALUE = 14;

  /**
   * The '<em><b>Anew</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ANEW
   * @model name="anew" literal="&lt;new_process&gt;"
   * @generated
   * @ordered
   */
  public static final int ANEW_VALUE = 15;

  /**
   * The '<em><b>Aclock</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACLOCK
   * @model name="aclock" literal="&lt;clock&gt;"
   * @generated
   * @ordered
   */
  public static final int ACLOCK_VALUE = 16;

  /**
   * The '<em><b>Apretake</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APRETAKE
   * @model name="apretake" literal="&lt;pre_take&gt;"
   * @generated
   * @ordered
   */
  public static final int APRETAKE_VALUE = 17;

  /**
   * The '<em><b>Aposttake</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APOSTTAKE
   * @model name="aposttake" literal="&lt;post_take&gt;"
   * @generated
   * @ordered
   */
  public static final int APOSTTAKE_VALUE = 18;

  /**
   * The '<em><b>Aschedulingaction</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ASCHEDULINGACTION
   * @model name="aschedulingaction" literal="&lt;scheduling_action&gt;"
   * @generated
   * @ordered
   */
  public static final int ASCHEDULINGACTION_VALUE = 19;

  /**
   * The '<em><b>Aprocessaction</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #APROCESSACTION
   * @model name="aprocessaction" literal="&lt;process_action&gt;"
   * @generated
   * @ordered
   */
  public static final int APROCESSACTION_VALUE = 20;

  /**
   * The '<em><b>Select</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SELECT
   * @model name="select" literal="select_process"
   * @generated
   * @ordered
   */
  public static final int SELECT_VALUE = 21;

  /**
   * The '<em><b>New</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NEW
   * @model name="new" literal="new_process"
   * @generated
   * @ordered
   */
  public static final int NEW_VALUE = 22;

  /**
   * The '<em><b>Pretake</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PRETAKE
   * @model name="pretake" literal="pre_take"
   * @generated
   * @ordered
   */
  public static final int PRETAKE_VALUE = 23;

  /**
   * The '<em><b>Posttake</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #POSTTAKE
   * @model name="posttake" literal="post_take"
   * @generated
   * @ordered
   */
  public static final int POSTTAKE_VALUE = 24;

  /**
   * An array of all the '<em><b>String</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final String[] VALUES_ARRAY =
    new String[]
    {
      TEMP,
      BYTE,
      INT,
      TIME,
      BOOL,
      CLOCK,
      PROCESS,
      QUEUE,
      STACK,
      NDBEHAVIOR,
      SEARCHING,
      ERROR,
      PROPERTY,
      ALL,
      ASELECT,
      ANEW,
      ACLOCK,
      APRETAKE,
      APOSTTAKE,
      ASCHEDULINGACTION,
      APROCESSACTION,
      SELECT,
      NEW,
      PRETAKE,
      POSTTAKE,
    };

  /**
   * A public read-only list of all the '<em><b>String</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<String> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>String</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static String get(java.lang.String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      String result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>String</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static String getByName(java.lang.String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      String result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>String</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static String get(int value)
  {
    switch (value)
    {
      case TEMP_VALUE: return TEMP;
      case BYTE_VALUE: return BYTE;
      case INT_VALUE: return INT;
      case TIME_VALUE: return TIME;
      case BOOL_VALUE: return BOOL;
      case CLOCK_VALUE: return CLOCK;
      case PROCESS_VALUE: return PROCESS;
      case QUEUE_VALUE: return QUEUE;
      case STACK_VALUE: return STACK;
      case NDBEHAVIOR_VALUE: return NDBEHAVIOR;
      case SEARCHING_VALUE: return SEARCHING;
      case ERROR_VALUE: return ERROR;
      case PROPERTY_VALUE: return PROPERTY;
      case ALL_VALUE: return ALL;
      case ASELECT_VALUE: return ASELECT;
      case ANEW_VALUE: return ANEW;
      case ACLOCK_VALUE: return ACLOCK;
      case APRETAKE_VALUE: return APRETAKE;
      case APOSTTAKE_VALUE: return APOSTTAKE;
      case ASCHEDULINGACTION_VALUE: return ASCHEDULINGACTION;
      case APROCESSACTION_VALUE: return APROCESSACTION;
      case SELECT_VALUE: return SELECT;
      case NEW_VALUE: return NEW;
      case PRETAKE_VALUE: return PRETAKE;
      case POSTTAKE_VALUE: return POSTTAKE;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final java.lang.String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final java.lang.String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private String(int value, java.lang.String name, java.lang.String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public java.lang.String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public java.lang.String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public java.lang.String toString()
  {
    return literal;
  }
  
} //String
