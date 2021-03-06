/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Event Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.EventDef#getEventname <em>Eventname</em>}</li>
 *   <li>{@link scheduling.dsl.EventDef#getProcessname <em>Processname</em>}</li>
 *   <li>{@link scheduling.dsl.EventDef#getEvent <em>Event</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getEventDef()
 * @model
 * @generated
 */
public interface EventDef extends EObject
{
  /**
   * Returns the value of the '<em><b>Eventname</b></em>' attribute.
   * The literals are from the enumeration {@link scheduling.dsl.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eventname</em>' attribute.
   * @see scheduling.dsl.String
   * @see #setEventname(scheduling.dsl.String)
   * @see scheduling.dsl.DslPackage#getEventDef_Eventname()
   * @model
   * @generated
   */
  scheduling.dsl.String getEventname();

  /**
   * Sets the value of the '{@link scheduling.dsl.EventDef#getEventname <em>Eventname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eventname</em>' attribute.
   * @see scheduling.dsl.String
   * @see #getEventname()
   * @generated
   */
  void setEventname(scheduling.dsl.String value);

  /**
   * Returns the value of the '<em><b>Processname</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Processname</em>' containment reference.
   * @see #setProcessname(scheduling.dsl.Process)
   * @see scheduling.dsl.DslPackage#getEventDef_Processname()
   * @model containment="true"
   * @generated
   */
  scheduling.dsl.Process getProcessname();

  /**
   * Sets the value of the '{@link scheduling.dsl.EventDef#getProcessname <em>Processname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Processname</em>' containment reference.
   * @see #getProcessname()
   * @generated
   */
  void setProcessname(scheduling.dsl.Process value);

  /**
   * Returns the value of the '<em><b>Event</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Event</em>' containment reference.
   * @see #setEvent(EObject)
   * @see scheduling.dsl.DslPackage#getEventDef_Event()
   * @model containment="true"
   * @generated
   */
  EObject getEvent();

  /**
   * Sets the value of the '{@link scheduling.dsl.EventDef#getEvent <em>Event</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Event</em>' containment reference.
   * @see #getEvent()
   * @generated
   */
  void setEvent(EObject value);

} // EventDef
