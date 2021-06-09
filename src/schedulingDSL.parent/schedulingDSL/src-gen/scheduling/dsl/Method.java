/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.Method#getFunctionname <em>Functionname</em>}</li>
 *   <li>{@link scheduling.dsl.Method#getParameterlist <em>Parameterlist</em>}</li>
 *   <li>{@link scheduling.dsl.Method#getAssignparameters <em>Assignparameters</em>}</li>
 *   <li>{@link scheduling.dsl.Method#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getMethod()
 * @model
 * @generated
 */
public interface Method extends EObject
{
  /**
   * Returns the value of the '<em><b>Functionname</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Functionname</em>' containment reference.
   * @see #setFunctionname(FunctionName)
   * @see scheduling.dsl.DslPackage#getMethod_Functionname()
   * @model containment="true"
   * @generated
   */
  FunctionName getFunctionname();

  /**
   * Sets the value of the '{@link scheduling.dsl.Method#getFunctionname <em>Functionname</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Functionname</em>' containment reference.
   * @see #getFunctionname()
   * @generated
   */
  void setFunctionname(FunctionName value);

  /**
   * Returns the value of the '<em><b>Parameterlist</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameterlist</em>' containment reference.
   * @see #setParameterlist(InterfaceParameterList)
   * @see scheduling.dsl.DslPackage#getMethod_Parameterlist()
   * @model containment="true"
   * @generated
   */
  InterfaceParameterList getParameterlist();

  /**
   * Sets the value of the '{@link scheduling.dsl.Method#getParameterlist <em>Parameterlist</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameterlist</em>' containment reference.
   * @see #getParameterlist()
   * @generated
   */
  void setParameterlist(InterfaceParameterList value);

  /**
   * Returns the value of the '<em><b>Assignparameters</b></em>' containment reference list.
   * The list contents are of type {@link scheduling.dsl.AssignParameters}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Assignparameters</em>' containment reference list.
   * @see scheduling.dsl.DslPackage#getMethod_Assignparameters()
   * @model containment="true"
   * @generated
   */
  EList<AssignParameters> getAssignparameters();

  /**
   * Returns the value of the '<em><b>Constraints</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constraints</em>' containment reference.
   * @see #setConstraints(Constraints)
   * @see scheduling.dsl.DslPackage#getMethod_Constraints()
   * @model containment="true"
   * @generated
   */
  Constraints getConstraints();

  /**
   * Sets the value of the '{@link scheduling.dsl.Method#getConstraints <em>Constraints</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constraints</em>' containment reference.
   * @see #getConstraints()
   * @generated
   */
  void setConstraints(Constraints value);

} // Method