/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scheduling.dsl.DslPackage;
import scheduling.dsl.InterfaceParameterDeclare;
import scheduling.dsl.InterfaceParameterList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interface Parameter List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.InterfaceParameterListImpl#getPara <em>Para</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InterfaceParameterListImpl extends MinimalEObjectImpl.Container implements InterfaceParameterList
{
  /**
   * The cached value of the '{@link #getPara() <em>Para</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPara()
   * @generated
   * @ordered
   */
  protected EList<InterfaceParameterDeclare> para;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InterfaceParameterListImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return DslPackage.eINSTANCE.getInterfaceParameterList();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<InterfaceParameterDeclare> getPara()
  {
    if (para == null)
    {
      para = new EObjectContainmentEList<InterfaceParameterDeclare>(InterfaceParameterDeclare.class, this, DslPackage.INTERFACE_PARAMETER_LIST__PARA);
    }
    return para;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case DslPackage.INTERFACE_PARAMETER_LIST__PARA:
        return ((InternalEList<?>)getPara()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case DslPackage.INTERFACE_PARAMETER_LIST__PARA:
        return getPara();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.INTERFACE_PARAMETER_LIST__PARA:
        getPara().clear();
        getPara().addAll((Collection<? extends InterfaceParameterDeclare>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.INTERFACE_PARAMETER_LIST__PARA:
        getPara().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case DslPackage.INTERFACE_PARAMETER_LIST__PARA:
        return para != null && !para.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //InterfaceParameterListImpl
