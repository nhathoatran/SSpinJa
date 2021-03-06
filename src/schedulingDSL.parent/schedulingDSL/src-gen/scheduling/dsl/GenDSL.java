/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Gen DSL</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.GenDSL#getGen <em>Gen</em>}</li>
 *   <li>{@link scheduling.dsl.GenDSL#getFunction <em>Function</em>}</li>
 *   <li>{@link scheduling.dsl.GenDSL#getIsr <em>Isr</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getGenDSL()
 * @model
 * @generated
 */
public interface GenDSL extends EObject
{
  /**
   * Returns the value of the '<em><b>Gen</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Gen</em>' containment reference.
   * @see #setGen(Generate)
   * @see scheduling.dsl.DslPackage#getGenDSL_Gen()
   * @model containment="true"
   * @generated
   */
  Generate getGen();

  /**
   * Sets the value of the '{@link scheduling.dsl.GenDSL#getGen <em>Gen</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gen</em>' containment reference.
   * @see #getGen()
   * @generated
   */
  void setGen(Generate value);

  /**
   * Returns the value of the '<em><b>Function</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Function</em>' containment reference.
   * @see #setFunction(InterfaceDef)
   * @see scheduling.dsl.DslPackage#getGenDSL_Function()
   * @model containment="true"
   * @generated
   */
  InterfaceDef getFunction();

  /**
   * Sets the value of the '{@link scheduling.dsl.GenDSL#getFunction <em>Function</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Function</em>' containment reference.
   * @see #getFunction()
   * @generated
   */
  void setFunction(InterfaceDef value);

  /**
   * Returns the value of the '<em><b>Isr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Isr</em>' containment reference.
   * @see #setIsr(ISR)
   * @see scheduling.dsl.DslPackage#getGenDSL_Isr()
   * @model containment="true"
   * @generated
   */
  ISR getIsr();

  /**
   * Sets the value of the '{@link scheduling.dsl.GenDSL#getIsr <em>Isr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Isr</em>' containment reference.
   * @see #getIsr()
   * @generated
   */
  void setIsr(ISR value);

} // GenDSL
