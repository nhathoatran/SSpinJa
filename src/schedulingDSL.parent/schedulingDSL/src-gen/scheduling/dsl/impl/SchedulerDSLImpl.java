/**
 * generated by Xtext 2.10.0
 */
package scheduling.dsl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import scheduling.dsl.DefCore;
import scheduling.dsl.DslPackage;
import scheduling.dsl.OrderingDef;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.Verify;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scheduler DSL</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link scheduling.dsl.impl.SchedulerDSLImpl#getDefcore <em>Defcore</em>}</li>
 *   <li>{@link scheduling.dsl.impl.SchedulerDSLImpl#getScheduler <em>Scheduler</em>}</li>
 *   <li>{@link scheduling.dsl.impl.SchedulerDSLImpl#getOrder <em>Order</em>}</li>
 *   <li>{@link scheduling.dsl.impl.SchedulerDSLImpl#getVerify <em>Verify</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SchedulerDSLImpl extends MinimalEObjectImpl.Container implements SchedulerDSL
{
  /**
   * The cached value of the '{@link #getDefcore() <em>Defcore</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefcore()
   * @generated
   * @ordered
   */
  protected DefCore defcore;

  /**
   * The cached value of the '{@link #getScheduler() <em>Scheduler</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScheduler()
   * @generated
   * @ordered
   */
  protected SchedulerDef scheduler;

  /**
   * The cached value of the '{@link #getOrder() <em>Order</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrder()
   * @generated
   * @ordered
   */
  protected OrderingDef order;

  /**
   * The cached value of the '{@link #getVerify() <em>Verify</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVerify()
   * @generated
   * @ordered
   */
  protected Verify verify;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SchedulerDSLImpl()
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
    return DslPackage.eINSTANCE.getSchedulerDSL();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DefCore getDefcore()
  {
    return defcore;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDefcore(DefCore newDefcore, NotificationChain msgs)
  {
    DefCore oldDefcore = defcore;
    defcore = newDefcore;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__DEFCORE, oldDefcore, newDefcore);
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
  public void setDefcore(DefCore newDefcore)
  {
    if (newDefcore != defcore)
    {
      NotificationChain msgs = null;
      if (defcore != null)
        msgs = ((InternalEObject)defcore).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__DEFCORE, null, msgs);
      if (newDefcore != null)
        msgs = ((InternalEObject)newDefcore).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__DEFCORE, null, msgs);
      msgs = basicSetDefcore(newDefcore, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__DEFCORE, newDefcore, newDefcore));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SchedulerDef getScheduler()
  {
    return scheduler;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetScheduler(SchedulerDef newScheduler, NotificationChain msgs)
  {
    SchedulerDef oldScheduler = scheduler;
    scheduler = newScheduler;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__SCHEDULER, oldScheduler, newScheduler);
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
  public void setScheduler(SchedulerDef newScheduler)
  {
    if (newScheduler != scheduler)
    {
      NotificationChain msgs = null;
      if (scheduler != null)
        msgs = ((InternalEObject)scheduler).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__SCHEDULER, null, msgs);
      if (newScheduler != null)
        msgs = ((InternalEObject)newScheduler).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__SCHEDULER, null, msgs);
      msgs = basicSetScheduler(newScheduler, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__SCHEDULER, newScheduler, newScheduler));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OrderingDef getOrder()
  {
    return order;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOrder(OrderingDef newOrder, NotificationChain msgs)
  {
    OrderingDef oldOrder = order;
    order = newOrder;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__ORDER, oldOrder, newOrder);
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
  public void setOrder(OrderingDef newOrder)
  {
    if (newOrder != order)
    {
      NotificationChain msgs = null;
      if (order != null)
        msgs = ((InternalEObject)order).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__ORDER, null, msgs);
      if (newOrder != null)
        msgs = ((InternalEObject)newOrder).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__ORDER, null, msgs);
      msgs = basicSetOrder(newOrder, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__ORDER, newOrder, newOrder));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Verify getVerify()
  {
    return verify;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVerify(Verify newVerify, NotificationChain msgs)
  {
    Verify oldVerify = verify;
    verify = newVerify;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__VERIFY, oldVerify, newVerify);
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
  public void setVerify(Verify newVerify)
  {
    if (newVerify != verify)
    {
      NotificationChain msgs = null;
      if (verify != null)
        msgs = ((InternalEObject)verify).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__VERIFY, null, msgs);
      if (newVerify != null)
        msgs = ((InternalEObject)newVerify).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DslPackage.SCHEDULER_DSL__VERIFY, null, msgs);
      msgs = basicSetVerify(newVerify, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, DslPackage.SCHEDULER_DSL__VERIFY, newVerify, newVerify));
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
      case DslPackage.SCHEDULER_DSL__DEFCORE:
        return basicSetDefcore(null, msgs);
      case DslPackage.SCHEDULER_DSL__SCHEDULER:
        return basicSetScheduler(null, msgs);
      case DslPackage.SCHEDULER_DSL__ORDER:
        return basicSetOrder(null, msgs);
      case DslPackage.SCHEDULER_DSL__VERIFY:
        return basicSetVerify(null, msgs);
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
      case DslPackage.SCHEDULER_DSL__DEFCORE:
        return getDefcore();
      case DslPackage.SCHEDULER_DSL__SCHEDULER:
        return getScheduler();
      case DslPackage.SCHEDULER_DSL__ORDER:
        return getOrder();
      case DslPackage.SCHEDULER_DSL__VERIFY:
        return getVerify();
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
      case DslPackage.SCHEDULER_DSL__DEFCORE:
        setDefcore((DefCore)newValue);
        return;
      case DslPackage.SCHEDULER_DSL__SCHEDULER:
        setScheduler((SchedulerDef)newValue);
        return;
      case DslPackage.SCHEDULER_DSL__ORDER:
        setOrder((OrderingDef)newValue);
        return;
      case DslPackage.SCHEDULER_DSL__VERIFY:
        setVerify((Verify)newValue);
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
      case DslPackage.SCHEDULER_DSL__DEFCORE:
        setDefcore((DefCore)null);
        return;
      case DslPackage.SCHEDULER_DSL__SCHEDULER:
        setScheduler((SchedulerDef)null);
        return;
      case DslPackage.SCHEDULER_DSL__ORDER:
        setOrder((OrderingDef)null);
        return;
      case DslPackage.SCHEDULER_DSL__VERIFY:
        setVerify((Verify)null);
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
      case DslPackage.SCHEDULER_DSL__DEFCORE:
        return defcore != null;
      case DslPackage.SCHEDULER_DSL__SCHEDULER:
        return scheduler != null;
      case DslPackage.SCHEDULER_DSL__ORDER:
        return order != null;
      case DslPackage.SCHEDULER_DSL__VERIFY:
        return verify != null;
    }
    return super.eIsSet(featureID);
  }

} //SchedulerDSLImpl