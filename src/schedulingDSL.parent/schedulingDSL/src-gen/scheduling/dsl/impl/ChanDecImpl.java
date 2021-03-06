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

import scheduling.dsl.ChanDec;
import scheduling.dsl.DslPackage;
import scheduling.dsl.NumValue;
import scheduling.dsl.TypeName;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Chan Dec</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.ChanDecImpl#getCname <em>Cname</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ChanDecImpl#getIvalue <em>Ivalue</em>}</li>
 *   <li>{@link scheduling.dsl.impl.ChanDecImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChanDecImpl extends MinimalEObjectImpl.Container implements ChanDec
{
  /**
   * The default value of the '{@link #getCname() <em>Cname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCname()
   * @generated
   * @ordered
   */
  protected static final String CNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCname() <em>Cname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCname()
   * @generated
   * @ordered
   */
  protected String cname = CNAME_EDEFAULT;

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
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final TypeName TYPE_EDEFAULT = TypeName.BYTE;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected TypeName type = TYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ChanDecImpl()
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
    return DslPackage.eINSTANCE.getChanDec();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getCname()
  {
    return cname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCname(String newCname)
  {
    String oldCname = cname;
    cname = newCname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CHAN_DEC__CNAME, oldCname, cname));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.CHAN_DEC__IVALUE, oldIvalue, newIvalue);
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
        msgs = ((InternalEObject)ivalue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.CHAN_DEC__IVALUE, null, msgs);
      if (newIvalue != null)
        msgs = ((InternalEObject)newIvalue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.CHAN_DEC__IVALUE, null, msgs);
      msgs = basicSetIvalue(newIvalue, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CHAN_DEC__IVALUE, newIvalue, newIvalue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TypeName getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setType(TypeName newType)
  {
    TypeName oldType = type;
    type = newType == null ? TYPE_EDEFAULT : newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.CHAN_DEC__TYPE, oldType, type));
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
      case DslPackage.CHAN_DEC__IVALUE:
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
      case DslPackage.CHAN_DEC__CNAME:
        return getCname();
      case DslPackage.CHAN_DEC__IVALUE:
        return getIvalue();
      case DslPackage.CHAN_DEC__TYPE:
        return getType();
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
      case DslPackage.CHAN_DEC__CNAME:
        setCname((String)newValue);
        return;
      case DslPackage.CHAN_DEC__IVALUE:
        setIvalue((NumValue)newValue);
        return;
      case DslPackage.CHAN_DEC__TYPE:
        setType((TypeName)newValue);
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
      case DslPackage.CHAN_DEC__CNAME:
        setCname(CNAME_EDEFAULT);
        return;
      case DslPackage.CHAN_DEC__IVALUE:
        setIvalue((NumValue)null);
        return;
      case DslPackage.CHAN_DEC__TYPE:
        setType(TYPE_EDEFAULT);
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
      case DslPackage.CHAN_DEC__CNAME:
        return CNAME_EDEFAULT == null ? cname != null : !CNAME_EDEFAULT.equals(cname);
      case DslPackage.CHAN_DEC__IVALUE:
        return ivalue != null;
      case DslPackage.CHAN_DEC__TYPE:
        return type != TYPE_EDEFAULT;
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
    result.append(" (cname: ");
    result.append(cname);
    result.append(", type: ");
    result.append(type);
    result.append(')');
    return result.toString();
  }

} //ChanDecImpl
