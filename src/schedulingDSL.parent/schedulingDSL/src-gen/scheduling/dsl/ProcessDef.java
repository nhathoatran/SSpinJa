/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ProcessDef#getProctype <em>Proctype</em>}</li>
 *   <li>{@link scheduling.dsl.ProcessDef#getParalist <em>Paralist</em>}</li>
 *   <li>{@link scheduling.dsl.ProcessDef#getPropertyassignment <em>Propertyassignment</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getProcessDef()
 * @model
 * @generated
 */
public interface ProcessDef extends EObject
{
  /**
   * Returns the value of the '<em><b>Proctype</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Proctype</em>' containment reference.
   * @see #setProctype(scheduling.dsl.Process)
   * @see scheduling.dsl.DslPackage#getProcessDef_Proctype()
   * @model containment="true"
   * @generated
   */
  scheduling.dsl.Process getProctype();

  /**
   * Sets the value of the '{@link scheduling.dsl.ProcessDef#getProctype <em>Proctype</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Proctype</em>' containment reference.
   * @see #getProctype()
   * @generated
   */
  void setProctype(scheduling.dsl.Process value);

  /**
   * Returns the value of the '<em><b>Paralist</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Paralist</em>' containment reference.
   * @see #setParalist(ParameterList)
   * @see scheduling.dsl.DslPackage#getProcessDef_Paralist()
   * @model containment="true"
   * @generated
   */
  ParameterList getParalist();

  /**
   * Sets the value of the '{@link scheduling.dsl.ProcessDef#getParalist <em>Paralist</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Paralist</em>' containment reference.
   * @see #getParalist()
   * @generated
   */
  void setParalist(ParameterList value);

  /**
   * Returns the value of the '<em><b>Propertyassignment</b></em>' containment reference list.
   * The list contents are of type {@link scheduling.dsl.PropertyAssignment}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Propertyassignment</em>' containment reference list.
   * @see scheduling.dsl.DslPackage#getProcessDef_Propertyassignment()
   * @model containment="true"
   * @generated
   */
  EList<PropertyAssignment> getPropertyassignment();

} // ProcessDef
