/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sporadic Process</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.SporadicProcess#getElement <em>Element</em>}</li>
 *   <li>{@link scheduling.dsl.SporadicProcess#getStart <em>Start</em>}</li>
 *   <li>{@link scheduling.dsl.SporadicProcess#getEnd <em>End</em>}</li>
 *   <li>{@link scheduling.dsl.SporadicProcess#getMax <em>Max</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getSporadicProcess()
 * @model
 * @generated
 */
public interface SporadicProcess extends EObject
{
  /**
   * Returns the value of the '<em><b>Element</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' containment reference.
   * @see #setElement(Element)
   * @see scheduling.dsl.DslPackage#getSporadicProcess_Element()
   * @model containment="true"
   * @generated
   */
  Element getElement();

  /**
   * Sets the value of the '{@link scheduling.dsl.SporadicProcess#getElement <em>Element</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' containment reference.
   * @see #getElement()
   * @generated
   */
  void setElement(Element value);

  /**
   * Returns the value of the '<em><b>Start</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Start</em>' attribute.
   * @see #setStart(int)
   * @see scheduling.dsl.DslPackage#getSporadicProcess_Start()
   * @model
   * @generated
   */
  int getStart();

  /**
   * Sets the value of the '{@link scheduling.dsl.SporadicProcess#getStart <em>Start</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Start</em>' attribute.
   * @see #getStart()
   * @generated
   */
  void setStart(int value);

  /**
   * Returns the value of the '<em><b>End</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>End</em>' attribute.
   * @see #setEnd(int)
   * @see scheduling.dsl.DslPackage#getSporadicProcess_End()
   * @model
   * @generated
   */
  int getEnd();

  /**
   * Sets the value of the '{@link scheduling.dsl.SporadicProcess#getEnd <em>End</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>End</em>' attribute.
   * @see #getEnd()
   * @generated
   */
  void setEnd(int value);

  /**
   * Returns the value of the '<em><b>Max</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max</em>' attribute.
   * @see #setMax(int)
   * @see scheduling.dsl.DslPackage#getSporadicProcess_Max()
   * @model
   * @generated
   */
  int getMax();

  /**
   * Sets the value of the '{@link scheduling.dsl.SporadicProcess#getMax <em>Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max</em>' attribute.
   * @see #getMax()
   * @generated
   */
  void setMax(int value);

} // SporadicProcess
