/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import java.lang.String;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import scheduling.dsl.BoolValue;
import scheduling.dsl.DslPackage;
import scheduling.dsl.NumValue;
import scheduling.dsl.RefListItem;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ref List Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.RefListItemImpl#getBvalue <em>Bvalue</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RefListItemImpl#getIvalue <em>Ivalue</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RefListItemImpl#getId <em>Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RefListItemImpl extends MinimalEObjectImpl.Container implements RefListItem
{
  /**
   * The cached value of the '{@link #getBvalue() <em>Bvalue</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBvalue()
   * @generated
   * @ordered
   */
  protected BoolValue bvalue;

  /**
   * The cached value of the '{@link #getIvalue() <em>Ivalue</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIvalue()
   * @generated
   * @ordered
   */
  protected NumValue ivalue;

  /**
   * The default value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
  protected String id = ID_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RefListItemImpl()
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
    return DslPackage.eINSTANCE.getRefListItem();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BoolValue getBvalue()
  {
    return bvalue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBvalue(BoolValue newBvalue, NotificationChain msgs)
  {
    BoolValue oldBvalue = bvalue;
    bvalue = newBvalue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.REF_LIST_ITEM__BVALUE, oldBvalue, newBvalue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBvalue(BoolValue newBvalue)
  {
    if (newBvalue != bvalue)
    {
      NotificationChain msgs = null;
      if (bvalue != null)
        msgs = ((InternalEObject)bvalue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.REF_LIST_ITEM__BVALUE, null, msgs);
      if (newBvalue != null)
        msgs = ((InternalEObject)newBvalue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.REF_LIST_ITEM__BVALUE, null, msgs);
      msgs = basicSetBvalue(newBvalue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.REF_LIST_ITEM__BVALUE, newBvalue, newBvalue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NumValue getIvalue()
  {
    return ivalue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIvalue(NumValue newIvalue, NotificationChain msgs)
  {
    NumValue oldIvalue = ivalue;
    ivalue = newIvalue;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.REF_LIST_ITEM__IVALUE, oldIvalue, newIvalue);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setIvalue(NumValue newIvalue)
  {
    if (newIvalue != ivalue)
    {
      NotificationChain msgs = null;
      if (ivalue != null)
        msgs = ((InternalEObject)ivalue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.REF_LIST_ITEM__IVALUE, null, msgs);
      if (newIvalue != null)
        msgs = ((InternalEObject)newIvalue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.REF_LIST_ITEM__IVALUE, null, msgs);
      msgs = basicSetIvalue(newIvalue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.REF_LIST_ITEM__IVALUE, newIvalue, newIvalue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setId(String newId)
  {
    String oldId = id;
    id = newId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.REF_LIST_ITEM__ID, oldId, id));
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
      case DslPackage.REF_LIST_ITEM__BVALUE:
        return basicSetBvalue(null, msgs);
      case DslPackage.REF_LIST_ITEM__IVALUE:
        return basicSetIvalue(null, msgs);
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
      case DslPackage.REF_LIST_ITEM__BVALUE:
        return getBvalue();
      case DslPackage.REF_LIST_ITEM__IVALUE:
        return getIvalue();
      case DslPackage.REF_LIST_ITEM__ID:
        return getId();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case DslPackage.REF_LIST_ITEM__BVALUE:
        setBvalue((BoolValue)newValue);
        return;
      case DslPackage.REF_LIST_ITEM__IVALUE:
        setIvalue((NumValue)newValue);
        return;
      case DslPackage.REF_LIST_ITEM__ID:
        setId((String)newValue);
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
      case DslPackage.REF_LIST_ITEM__BVALUE:
        setBvalue((BoolValue)null);
        return;
      case DslPackage.REF_LIST_ITEM__IVALUE:
        setIvalue((NumValue)null);
        return;
      case DslPackage.REF_LIST_ITEM__ID:
        setId(ID_EDEFAULT);
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
      case DslPackage.REF_LIST_ITEM__BVALUE:
        return bvalue != null;
      case DslPackage.REF_LIST_ITEM__IVALUE:
        return ivalue != null;
      case DslPackage.REF_LIST_ITEM__ID:
        return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (id: ");
    result.append(id);
    result.append(')');
    return result.toString();
  }

} //RefListItemImpl
