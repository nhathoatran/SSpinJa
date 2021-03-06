/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.ReturnStatement#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see scheduling.dsl.DslPackage#getReturnStatement()
 * @model
 * @generated
 */
public interface ReturnStatement extends Statement
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * The literals are from the enumeration {@link scheduling.dsl.OrderType}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see scheduling.dsl.OrderType
   * @see #setValue(OrderType)
   * @see scheduling.dsl.DslPackage#getReturnStatement_Value()
   * @model
   * @generated
   */
  OrderType getValue();

  /**
   * Sets the value of the '{@link scheduling.dsl.ReturnStatement#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see scheduling.dsl.OrderType
   * @see #getValue()
   * @generated
   */
  void setValue(OrderType value);

} // ReturnStatement
