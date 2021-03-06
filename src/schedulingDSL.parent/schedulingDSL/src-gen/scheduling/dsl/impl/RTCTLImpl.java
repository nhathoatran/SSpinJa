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

import scheduling.dsl.DslPackage;
import scheduling.dsl.Expression;
import scheduling.dsl.LTE;
import scheduling.dsl.RTCTL;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RTCTL</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getOp <em>Op</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getExp <em>Exp</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getF <em>F</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getF1 <em>F1</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getF2 <em>F2</em>}</li>
 *   <li>{@link scheduling.dsl.impl.RTCTLImpl#getLte <em>Lte</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RTCTLImpl extends MinimalEObjectImpl.Container implements RTCTL
{
  /**
   * The default value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected static final String OP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOp() <em>Op</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp()
   * @generated
   * @ordered
   */
  protected String op = OP_EDEFAULT;

  /**
   * The cached value of the '{@link #getExp() <em>Exp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExp()
   * @generated
   * @ordered
   */
  protected Expression exp;

  /**
   * The cached value of the '{@link #getF() <em>F</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getF()
   * @generated
   * @ordered
   */
  protected RTCTL f;

  /**
   * The cached value of the '{@link #getF1() <em>F1</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getF1()
   * @generated
   * @ordered
   */
  protected RTCTL f1;

  /**
   * The cached value of the '{@link #getF2() <em>F2</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getF2()
   * @generated
   * @ordered
   */
  protected RTCTL f2;

  /**
   * The cached value of the '{@link #getLte() <em>Lte</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLte()
   * @generated
   * @ordered
   */
  protected LTE lte;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RTCTLImpl()
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
    return DslPackage.eINSTANCE.getRTCTL();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getOp()
  {
    return op;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setOp(String newOp)
  {
    String oldOp = op;
    op = newOp;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__OP, oldOp, op));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getExp()
  {
    return exp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExp(Expression newExp, NotificationChain msgs)
  {
    Expression oldExp = exp;
    exp = newExp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__EXP, oldExp, newExp);
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
  public void setExp(Expression newExp)
  {
    if (newExp != exp)
    {
      NotificationChain msgs = null;
      if (exp != null)
        msgs = ((InternalEObject)exp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__EXP, null, msgs);
      if (newExp != null)
        msgs = ((InternalEObject)newExp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__EXP, null, msgs);
      msgs = basicSetExp(newExp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__EXP, newExp, newExp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RTCTL getF()
  {
    return f;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetF(RTCTL newF, NotificationChain msgs)
  {
    RTCTL oldF = f;
    f = newF;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F, oldF, newF);
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
  public void setF(RTCTL newF)
  {
    if (newF != f)
    {
      NotificationChain msgs = null;
      if (f != null)
        msgs = ((InternalEObject)f).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F, null, msgs);
      if (newF != null)
        msgs = ((InternalEObject)newF).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F, null, msgs);
      msgs = basicSetF(newF, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F, newF, newF));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RTCTL getF1()
  {
    return f1;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetF1(RTCTL newF1, NotificationChain msgs)
  {
    RTCTL oldF1 = f1;
    f1 = newF1;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F1, oldF1, newF1);
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
  public void setF1(RTCTL newF1)
  {
    if (newF1 != f1)
    {
      NotificationChain msgs = null;
      if (f1 != null)
        msgs = ((InternalEObject)f1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F1, null, msgs);
      if (newF1 != null)
        msgs = ((InternalEObject)newF1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F1, null, msgs);
      msgs = basicSetF1(newF1, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F1, newF1, newF1));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RTCTL getF2()
  {
    return f2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetF2(RTCTL newF2, NotificationChain msgs)
  {
    RTCTL oldF2 = f2;
    f2 = newF2;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F2, oldF2, newF2);
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
  public void setF2(RTCTL newF2)
  {
    if (newF2 != f2)
    {
      NotificationChain msgs = null;
      if (f2 != null)
        msgs = ((InternalEObject)f2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F2, null, msgs);
      if (newF2 != null)
        msgs = ((InternalEObject)newF2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__F2, null, msgs);
      msgs = basicSetF2(newF2, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__F2, newF2, newF2));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public LTE getLte()
  {
    return lte;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetLte(LTE newLte, NotificationChain msgs)
  {
    LTE oldLte = lte;
    lte = newLte;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__LTE, oldLte, newLte);
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
  public void setLte(LTE newLte)
  {
    if (newLte != lte)
    {
      NotificationChain msgs = null;
      if (lte != null)
        msgs = ((InternalEObject)lte).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__LTE, null, msgs);
      if (newLte != null)
        msgs = ((InternalEObject)newLte).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.RTCTL__LTE, null, msgs);
      msgs = basicSetLte(newLte, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.RTCTL__LTE, newLte, newLte));
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
      case DslPackage.RTCTL__EXP:
        return basicSetExp(null, msgs);
      case DslPackage.RTCTL__F:
        return basicSetF(null, msgs);
      case DslPackage.RTCTL__F1:
        return basicSetF1(null, msgs);
      case DslPackage.RTCTL__F2:
        return basicSetF2(null, msgs);
      case DslPackage.RTCTL__LTE:
        return basicSetLte(null, msgs);
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
      case DslPackage.RTCTL__OP:
        return getOp();
      case DslPackage.RTCTL__EXP:
        return getExp();
      case DslPackage.RTCTL__F:
        return getF();
      case DslPackage.RTCTL__F1:
        return getF1();
      case DslPackage.RTCTL__F2:
        return getF2();
      case DslPackage.RTCTL__LTE:
        return getLte();
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
      case DslPackage.RTCTL__OP:
        setOp((String)newValue);
        return;
      case DslPackage.RTCTL__EXP:
        setExp((Expression)newValue);
        return;
      case DslPackage.RTCTL__F:
        setF((RTCTL)newValue);
        return;
      case DslPackage.RTCTL__F1:
        setF1((RTCTL)newValue);
        return;
      case DslPackage.RTCTL__F2:
        setF2((RTCTL)newValue);
        return;
      case DslPackage.RTCTL__LTE:
        setLte((LTE)newValue);
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
      case DslPackage.RTCTL__OP:
        setOp(OP_EDEFAULT);
        return;
      case DslPackage.RTCTL__EXP:
        setExp((Expression)null);
        return;
      case DslPackage.RTCTL__F:
        setF((RTCTL)null);
        return;
      case DslPackage.RTCTL__F1:
        setF1((RTCTL)null);
        return;
      case DslPackage.RTCTL__F2:
        setF2((RTCTL)null);
        return;
      case DslPackage.RTCTL__LTE:
        setLte((LTE)null);
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
      case DslPackage.RTCTL__OP:
        return OP_EDEFAULT == null ? op != null : !OP_EDEFAULT.equals(op);
      case DslPackage.RTCTL__EXP:
        return exp != null;
      case DslPackage.RTCTL__F:
        return f != null;
      case DslPackage.RTCTL__F1:
        return f1 != null;
      case DslPackage.RTCTL__F2:
        return f2 != null;
      case DslPackage.RTCTL__LTE:
        return lte != null;
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
    result.append(" (op: ");
    result.append(op);
    result.append(')');
    return result.toString();
  }

} //RTCTLImpl
