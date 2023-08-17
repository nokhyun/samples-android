//package com.nokhyun.third.composable
//
//import android.content.Context
//import android.os.IBinder
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewGroup
//import android.view.ViewParent
//import androidx.compose.runtime.Applier
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.Composition
//import androidx.compose.runtime.CompositionContext
//import androidx.compose.runtime.Recomposer
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.InternalComposeUiApi
//import androidx.compose.ui.UiComposable
//import androidx.compose.ui.autofill.Autofill
//import androidx.compose.ui.autofill.AutofillTree
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Canvas
//import androidx.compose.ui.hapticfeedback.HapticFeedback
//import androidx.compose.ui.input.InputModeManager
//import androidx.compose.ui.input.key.KeyEvent
//import androidx.compose.ui.node.InternalCoreApi
//import androidx.compose.ui.node.RootForTest
//import androidx.compose.ui.platform.AccessibilityManager
//import androidx.compose.ui.platform.ClipboardManager
//import androidx.compose.ui.platform.ComposeView
//import androidx.compose.ui.platform.TextToolbar
//import androidx.compose.ui.platform.ViewConfiguration
//import androidx.compose.ui.platform.WindowInfo
//import androidx.compose.ui.platform.WindowRecomposerFactory
//import androidx.compose.ui.platform.WindowRecomposerPolicy
//import androidx.compose.ui.platform.compositionContext
//import androidx.compose.ui.platform.findViewTreeCompositionContext
//import androidx.compose.ui.text.ExperimentalTextApi
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.PlatformTextInputPluginRegistry
//import androidx.compose.ui.text.input.TextInputService
//import androidx.compose.ui.unit.Constraints
//import androidx.compose.ui.unit.Density
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.customview.poolingcontainer.PoolingContainerListener
//import androidx.customview.poolingcontainer.addPoolingContainerListener
//import androidx.customview.poolingcontainer.isWithinPoolingContainer
//import androidx.customview.poolingcontainer.removePoolingContainerListener
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleEventObserver
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.findViewTreeLifecycleOwner
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.android.asCoroutineDispatcher
//import kotlinx.coroutines.launch
//import java.lang.ref.WeakReference
//import java.util.concurrent.atomic.AtomicReference
//
//abstract class AbstractComposeView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : ViewGroup(context, attrs, defStyleAttr) {
//
//    init {
//        clipChildren = false
//        clipToPadding = false
//    }
//
//    /**
//     * The first time we successfully locate this we'll save it here.
//     * If this View moves to the [android.view.ViewOverlay] we won't be able
//     * to find view tree dependencies; this happens when using transition APIs
//     * to animate views out in particular.
//     *
//     * We only ever set this when we're attached to a window.
//     */
//    private var cachedViewTreeCompositionContext: WeakReference<CompositionContext>? = null
//
//    /**
//     * The [getWindowToken] of the window this view was last attached to.
//     * If we become attached to a new window we clear [cachedViewTreeCompositionContext]
//     * so that we might appeal to the (possibly lazily created) [windowRecomposer]
//     * if [findViewTreeCompositionContext] can't locate one instead of using the previous
//     * [cachedViewTreeCompositionContext].
//     */
//    private var previousAttachedWindowToken: IBinder? = null
//        set(value) {
//            if (field !== value) {
//                field = value
//                cachedViewTreeCompositionContext = null
//            }
//        }
//
//    private var composition: Composition? = null
//
//    /**
//     * The explicitly set [CompositionContext] to use as the parent of compositions created
//     * for this view. Set by [setParentCompositionContext].
//     *
//     * If set to a non-null value [cachedViewTreeCompositionContext] will be cleared.
//     */
//    private var parentContext: CompositionContext? = null
//        set(value) {
//            if (field !== value) {
//                field = value
//                if (value != null) {
//                    cachedViewTreeCompositionContext = null
//                }
//                val old = composition
//                if (old !== null) {
//                    old.dispose()
//                    composition = null
//
//                    // Recreate the composition now if we are attached.
//                    if (isAttachedToWindow) {
//                        ensureCompositionCreated()
//                    }
//                }
//            }
//        }
//
//    /**
//     * Set the [CompositionContext] that should be the parent of this view's composition.
//     * If [parent] is `null` it will be determined automatically from the window the view is
//     * attached to.
//     */
//    fun setParentCompositionContext(parent: CompositionContext?) {
//        parentContext = parent
//    }
//
//    // Leaking `this` during init is generally dangerous, but we know that the implementation of
//    // this particular ViewCompositionStrategy is not going to do something harmful with it.
//    @Suppress("LeakingThis")
//    private var disposeViewCompositionStrategy: (() -> Unit)? =
//        ViewCompositionStrategy.Default.installFor(this)
//
//    /**
//     * Set the strategy for managing disposal of this View's internal composition.
//     * Defaults to [ViewCompositionStrategy.Default].
//     *
//     * This View's composition is a live resource that must be disposed to ensure that
//     * long-lived references to it do not persist
//     *
//     * See [ViewCompositionStrategy] for more information.
//     */
//    fun setViewCompositionStrategy(strategy: ViewCompositionStrategy) {
//        disposeViewCompositionStrategy?.invoke()
//        disposeViewCompositionStrategy = strategy.installFor(this)
//    }
//
//    /**
//     * If `true`, this View's composition will be created when it becomes attached to a
//     * window for the first time. Defaults to `true`.
//     *
//     * Subclasses may choose to override this property to prevent this eager initial composition
//     * in cases where the view's content is not yet ready. Initial composition will still occur
//     * when this view is first measured.
//     */
//    protected open val shouldCreateCompositionOnAttachedToWindow: Boolean
//        get() = true
//
//    /**
//     * Enables the display of visual layout bounds for the Compose UI content of this view.
//     * This is typically managed
//     */
//    @OptIn(InternalCoreApi::class)
//    @InternalComposeUiApi
//    @Suppress("GetterSetterNames")
//    @get:Suppress("GetterSetterNames")
//    var showLayoutBounds: Boolean = false
//        set(value) {
//            field = value
//            getChildAt(0)?.let {
//                (it as Owner).showLayoutBounds = value
//            }
//        }
//
//    /**
//     * The Jetpack Compose UI content for this view.
//     * Subclasses must implement this method to provide content. Initial composition will
//     * occur when the view becomes attached to a window or when [createComposition] is called,
//     * whichever comes first.
//     */
//    @Composable
//    @UiComposable
//    abstract fun Content()
//
//    /**
//     * Perform initial composition for this view.
//     * Once this method is called or the view becomes attached to a window,
//     * either [disposeComposition] must be called or the
//     * [LifecycleOwner] returned by [findViewTreeLifecycleOwner] must
//     * reach the [Lifecycle.State.DESTROYED] state for the composition to be cleaned up
//     * properly. (This restriction is temporary.)
//     *
//     * If this method is called when the composition has already been created it has no effect.
//     *
//     * This method should only be called if this view [isAttachedToWindow] or if a parent
//     * [CompositionContext] has been [set][setParentCompositionContext] explicitly.
//     */
//    fun createComposition() {
//        check(parentContext != null || isAttachedToWindow) {
//            "createComposition requires either a parent reference or the View to be attached" +
//                    "to a window. Attach the View or call setParentCompositionReference."
//        }
//        ensureCompositionCreated()
//    }
//
//    private var creatingComposition = false
//    private fun checkAddView() {
//        if (!creatingComposition) {
//            throw UnsupportedOperationException(
//                "Cannot add views to " +
//                        "${javaClass.simpleName}; only Compose content is supported"
//            )
//        }
//    }
//
//    /**
//     * `true` if the [CompositionContext] can be considered to be "alive" for the purposes
//     * of locally caching it in case the view is placed into a ViewOverlay.
//     * [Recomposer]s that are in the [Recomposer.State.ShuttingDown] state or lower should
//     * not be cached or reusedif currently cached, as they will never recompose content.
//     */
//    private val CompositionContext.isAlive: Boolean
//        get() = this !is Recomposer || currentState.value > Recomposer.State.ShuttingDown
//
//    /**
//     * Cache this [CompositionContext] in [cachedViewTreeCompositionContext] if it [isAlive]
//     * and return the [CompositionContext] itself either way.
//     */
//    private fun CompositionContext.cacheIfAlive(): CompositionContext = also { context ->
//        context.takeIf { it.isAlive }
//            ?.let { cachedViewTreeCompositionContext = WeakReference(it) }
//    }
//
//    /**
//     * Determine the correct [CompositionContext] to use as the parent of this view's
//     * composition. This can result in caching a looked-up [CompositionContext] for use
//     * later. See [cachedViewTreeCompositionContext] for more details.
//     *
//     * If [cachedViewTreeCompositionContext] is available but [findViewTreeCompositionContext]
//     * cannot find a parent context, we will use the cached context if present before appealing
//     * to the [windowRecomposer], as [windowRecomposer] can lazily create a recomposer.
//     * If we're reattached to the same window and [findViewTreeCompositionContext] can't find the
//     * context that [windowRecomposer] would install, we might be in the [getOverlay] of some
//     * part of the view hierarchy to animate the disappearance of this and other views. We still
//     * need to be able to compose/recompose in this state without creating a brand new recomposer
//     * to do it, as well as still locate any view tree dependencies.
//     */
//    private fun resolveParentCompositionContext() = parentContext
//        ?: findViewTreeCompositionContext()?.cacheIfAlive()
//        ?: cachedViewTreeCompositionContext?.get()?.takeIf { it.isAlive }
//        ?: windowRecomposer.cacheIfAlive()
//
//    @Suppress("DEPRECATION") // Still using ViewGroup.setContent for now
//    private fun ensureCompositionCreated() {
//        if (composition == null) {
//            try {
//                creatingComposition = true
//                composition = setContent(resolveParentCompositionContext()) {
//                    Content()
//                }
//            } finally {
//                creatingComposition = false
//            }
//        }
//    }
//
//    /**
//     * Dispose of the underlying composition and [requestLayout].
//     * A new composition will be created if [createComposition] is called or when needed to
//     * lay out this view.
//     */
//    fun disposeComposition() {
//        composition?.dispose()
//        composition = null
//        requestLayout()
//    }
//
//    /**
//     * `true` if this View is host to an active Compose UI composition.
//     * An active composition may consume resources.
//     */
//    val hasComposition: Boolean get() = composition != null
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//
//        previousAttachedWindowToken = windowToken
//
//        if (shouldCreateCompositionOnAttachedToWindow) {
//            ensureCompositionCreated()
//        }
//    }
//
//    final override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        ensureCompositionCreated()
//        internalOnMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
//
//    @Suppress("WrongCall")
//    internal open fun internalOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val child = getChildAt(0)
//        if (child == null) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//            return
//        }
//
//        val width = maxOf(0, MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight)
//        val height = maxOf(0, MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom)
//        child.measure(
//            MeasureSpec.makeMeasureSpec(width, MeasureSpec.getMode(widthMeasureSpec)),
//            MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)),
//        )
//        setMeasuredDimension(
//            child.measuredWidth + paddingLeft + paddingRight,
//            child.measuredHeight + paddingTop + paddingBottom
//        )
//    }
//
//    final override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) =
//        internalOnLayout(changed, left, top, right, bottom)
//
//    internal open fun internalOnLayout(
//        changed: Boolean,
//        left: Int,
//        top: Int,
//        right: Int,
//        bottom: Int
//    ) {
//        getChildAt(0)?.layout(
//            paddingLeft,
//            paddingTop,
//            right - left - paddingRight,
//            bottom - top - paddingBottom
//        )
//    }
//
//    override fun onRtlPropertiesChanged(layoutDirection: Int) {
//        // Force the single child for our composition to have the same LayoutDirection
//        // that we do. We will get onRtlPropertiesChanged eagerly as the value changes,
//        // but the composition child view won't until it measures. This can be too late
//        // to catch the composition pass for that frame, so propagate it eagerly.
//        getChildAt(0)?.layoutDirection = layoutDirection
//    }
//
//    // Transition group handling:
//    // Both the framework and androidx transition APIs use isTransitionGroup as a signal for
//    // determining view properties to capture during a transition. As AbstractComposeView uses
//    // a view subhierarchy to perform its work but operates as a single unit, mark instances as
//    // transition groups by default.
//    // This is implemented as overridden methods instead of setting isTransitionGroup = true in
//    // the constructor so that values set explicitly by xml inflation performed by the ViewGroup
//    // constructor will take precedence. As of this writing all known framework implementations
//    // use the public isTransitionGroup method rather than checking the internal ViewGroup flag
//    // to determine behavior, making this implementation a slight compatibility risk for a
//    // tradeoff of cleaner View-consumer API behavior without the overhead of performing an
//    // additional obtainStyledAttributes call to determine a value potentially overridden from xml.
//
//    private var isTransitionGroupSet = false
//
//    override fun isTransitionGroup(): Boolean = !isTransitionGroupSet || super.isTransitionGroup()
//
//    override fun setTransitionGroup(isTransitionGroup: Boolean) {
//        super.setTransitionGroup(isTransitionGroup)
//        isTransitionGroupSet = true
//    }
//
//    // Below: enforce restrictions on adding child views to this ViewGroup
//
//    override fun addView(child: View?) {
//        checkAddView()
//        super.addView(child)
//    }
//
//    override fun addView(child: View?, index: Int) {
//        checkAddView()
//        super.addView(child, index)
//    }
//
//    override fun addView(child: View?, width: Int, height: Int) {
//        checkAddView()
//        super.addView(child, width, height)
//    }
//
//    override fun addView(child: View?, params: LayoutParams?) {
//        checkAddView()
//        super.addView(child, params)
//    }
//
//    override fun addView(child: View?, index: Int, params: LayoutParams?) {
//        checkAddView()
//        super.addView(child, index, params)
//    }
//
//    override fun addViewInLayout(child: View?, index: Int, params: LayoutParams?): Boolean {
//        checkAddView()
//        return super.addViewInLayout(child, index, params)
//    }
//
//    override fun addViewInLayout(
//        child: View?,
//        index: Int,
//        params: LayoutParams?,
//        preventRequestLayout: Boolean
//    ): Boolean {
//        checkAddView()
//        return super.addViewInLayout(child, index, params, preventRequestLayout)
//    }
//
//    override fun shouldDelayChildPressedState(): Boolean = false
//}
//
//
///**  */
//
//interface ViewCompositionStrategy {
//
//    /**
//     * Install this strategy for [view] and return a function that will uninstall it later.
//     * This function should not be called directly; it is called by
//     * [AbstractComposeView.setViewCompositionStrategy] after uninstalling the previous strategy.
//     */
//    fun installFor(view: AbstractComposeView): () -> Unit
//
//    /**
//     * This companion object may be used to define extension factory functions for other
//     * strategies to aid in discovery via autocomplete. e.g.:
//     * `fun ViewCompositionStrategy.Companion.MyStrategy(): MyStrategy`
//     */
//    companion object {
//        /**
//         * The default strategy for [AbstractComposeView] and [ComposeView].
//         *
//         * Currently, this is [DisposeOnDetachedFromWindowOrReleasedFromPool], though this
//         * implementation detail may change.
//         */
//        // WARNING: the implementation of the default strategy is installed with a reference to
//        // `this` on a not-fully-constructed object in AbstractComposeView.
//        // Be careful not to do anything that would break that.
//        val Default: ViewCompositionStrategy
//            get() = DisposeOnDetachedFromWindowOrReleasedFromPool
//    }
//
//    /**
//     * The composition will be disposed automatically when the view is detached from a window,
//     * unless it is part of a [pooling container][isPoolingContainer], such as `RecyclerView`.
//     *
//     * When not within a pooling container, this behaves exactly the same as
//     * [DisposeOnDetachedFromWindow].
//     */
//    // WARNING: the implementation of the default strategy is installed with a reference to
//    // `this` on a not-fully-constructed object in AbstractComposeView.
//    // Be careful not to do anything that would break that.
//    object DisposeOnDetachedFromWindowOrReleasedFromPool : ViewCompositionStrategy {
//        override fun installFor(view: AbstractComposeView): () -> Unit {
//            val listener = object : View.OnAttachStateChangeListener {
//                override fun onViewAttachedToWindow(v: View) {}
//
//                override fun onViewDetachedFromWindow(v: View) {
//                    if (!view.isWithinPoolingContainer) {
//                        view.disposeComposition()
//                    }
//                }
//            }
//            view.addOnAttachStateChangeListener(listener)
//
//            val poolingContainerListener = PoolingContainerListener { view.disposeComposition() }
//            view.addPoolingContainerListener(poolingContainerListener)
//
//            return {
//                view.removeOnAttachStateChangeListener(listener)
//                view.removePoolingContainerListener(poolingContainerListener)
//            }
//        }
//    }
//
//    /**
//     * [ViewCompositionStrategy] that disposes the composition whenever the view becomes detached
//     * from a window. If the user of a Compose UI view never explicitly calls
//     * [AbstractComposeView.createComposition], this strategy is always safe and will always
//     * clean up composition resources with no explicit action required - just use the view like
//     * any other View and let garbage collection do the rest. (If
//     * [AbstractComposeView.createComposition] is called while the view is detached from a window,
//     * [AbstractComposeView.disposeComposition] must be called manually if the view is not later
//     * attached to a window.)
//     */
//    object DisposeOnDetachedFromWindow : ViewCompositionStrategy {
//        override fun installFor(view: AbstractComposeView): () -> Unit {
//            val listener = object : View.OnAttachStateChangeListener {
//                override fun onViewAttachedToWindow(v: View) {}
//
//                override fun onViewDetachedFromWindow(v: View) {
//                    view.disposeComposition()
//                }
//            }
//            view.addOnAttachStateChangeListener(listener)
//            return { view.removeOnAttachStateChangeListener(listener) }
//        }
//    }
//
//    /**
//     * [ViewCompositionStrategy] that disposes the composition when [lifecycle] is
//     * [destroyed][Lifecycle.Event.ON_DESTROY]. This strategy is appropriate for Compose UI views
//     * that share a 1-1 relationship with a known [LifecycleOwner].
//     */
//    class DisposeOnLifecycleDestroyed(
//        private val lifecycle: Lifecycle
//    ) : ViewCompositionStrategy {
//        constructor(lifecycleOwner: LifecycleOwner) : this(lifecycleOwner.lifecycle)
//
//        override fun installFor(view: AbstractComposeView): () -> Unit =
//            installForLifecycle(view, lifecycle)
//
//    }
//
//    /**
//     * [ViewCompositionStrategy] that disposes the composition when the
//     * [LifecycleOwner] returned by [findViewTreeLifecycleOwner] of the next window
//     * the view is attached to is [destroyed][Lifecycle.Event.ON_DESTROY].
//     * This strategy is appropriate for Compose UI views that share a 1-1 relationship with
//     * their closest [LifecycleOwner], such as a Fragment view.
//     */
//    object DisposeOnViewTreeLifecycleDestroyed : ViewCompositionStrategy {
//        override fun installFor(view: AbstractComposeView): () -> Unit {
//            if (view.isAttachedToWindow) {
//                val lco = checkNotNull(view.findViewTreeLifecycleOwner()) {
//                    "View tree for $view has no ViewTreeLifecycleOwner"
//                }
//                return installForLifecycle(view, lco.lifecycle)
//            } else {
//                // We change this reference after we successfully attach
//                var disposer: () -> Unit
//                val listener = object : View.OnAttachStateChangeListener {
//                    override fun onViewAttachedToWindow(v: View) {
//                        val lco = checkNotNull(view.findViewTreeLifecycleOwner()) {
//                            "View tree for $view has no ViewTreeLifecycleOwner"
//                        }
//                        disposer = installForLifecycle(view, lco.lifecycle)
//
//                        // Ensure this runs only once
//                        view.removeOnAttachStateChangeListener(this)
//                    }
//
//                    override fun onViewDetachedFromWindow(v: View) {}
//                }
//                view.addOnAttachStateChangeListener(listener)
//                disposer = { view.removeOnAttachStateChangeListener(listener) }
//                return { disposer() }
//            }
//        }
//    }
//}
//
//private fun installForLifecycle(view: AbstractComposeView, lifecycle: Lifecycle): () -> Unit {
//    check(lifecycle.currentState > Lifecycle.State.DESTROYED) {
//        "Cannot configure $view to disposeComposition at Lifecycle ON_DESTROY: $lifecycle" +
//                "is already destroyed"
//    }
//    val observer = LifecycleEventObserver { _, event ->
//        if (event == Lifecycle.Event.ON_DESTROY) {
//            view.disposeComposition()
//        }
//    }
//    lifecycle.addObserver(observer)
//    return { lifecycle.removeObserver(observer) }
//}
//
///** 222*/
//
//internal interface Owner {
//
//    /**
//     * The root layout node in the component tree.
//     */
//    val root: LayoutNode
//
//    /**
//     * Draw scope reused for drawing speed up.
//     */
//    val sharedDrawScope: LayoutNodeDrawScope
//
//    val rootForTest: RootForTest
//
//    /**
//     * Provide haptic feedback to the user. Use the Android version of haptic feedback.
//     */
//    val hapticFeedBack: HapticFeedback
//
//    /**
//     * Provide information about the current input mode, and a way to programmatically change the
//     * input mode.
//     */
//    val inputModeManager: InputModeManager
//
//    /**
//     * Provide clipboard manager to the user. Use the Android version of clipboard manager.
//     */
//    val clipboardManager: ClipboardManager
//
//    /**
//     * Provide accessibility manager to the user. Use the Android version of accessibility manager.
//     */
//    val accessibilityManager: AccessibilityManager
//
//    /**
//     * Provide toolbar for text-related actions, such as copy, paste, cut etc.
//     */
//    val textToolbar: TextToolbar
//
//    /**
//     *  A data structure used to store autofill information. It is used by components that want to
//     *  provide autofill semantics.
//     *  TODO(ralu): Replace with SemanticsTree. This is a temporary hack until we have a semantics
//     *  tree implemented.
//     */
//    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
//    @get:ExperimentalComposeUiApi
//    @ExperimentalComposeUiApi
//    val autofillTree: AutofillTree
//
//    /**
//     * The [Autofill] class can be used to perform autofill operations. It is used as a
//     * CompositionLocal.
//     */
//    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
//    @get:ExperimentalComposeUiApi
//    @ExperimentalComposeUiApi
//    val autofill: Autofill?
//
//    val density: Density
//
//    val textInputService: TextInputService
//
//    @OptIn(ExperimentalTextApi::class)
//    val platformTextInputPluginRegistry: PlatformTextInputPluginRegistry
//
//    val pointerIconService: PointerIconService
//
//    /**
//     * Provide a focus owner that controls focus within Compose.
//     */
//    val focusOwner: FocusOwner
//
//    /**
//     * Provide information about the window that hosts this [Owner].
//     */
//    val windowInfo: WindowInfo
//
//    @Deprecated(
//        "fontLoader is deprecated, use fontFamilyResolver",
//        replaceWith = ReplaceWith("fontFamilyResolver")
//    )
//    @Suppress("DEPRECATION")
//    val fontLoader: Font.ResourceLoader
//
//    val fontFamilyResolver: FontFamily.Resolver
//
//    val layoutDirection: LayoutDirection
//
//    /**
//     * `true` when layout should draw debug bounds.
//     */
//    var showLayoutBounds: Boolean
//        /** @suppress */
//        @InternalCoreApi
//        set
//
//    /**
//     * Called by [LayoutNode] to request the Owner a new measurement+layout. [forceRequest] defines
//     * whether the node should bypass the logic that would reject measure requests, and therefore
//     * force the measure request to be evaluated even when it's already pending measure.
//     *
//     * [affectsLookahead] specifies whether this measure request is for the lookahead pass.
//     */
//    fun onRequestMeasure(
//        layoutNode: LayoutNode,
//        affectsLookahead: Boolean = false,
//        forceRequest: Boolean = false
//    )
//
//    /**
//     * Called by [LayoutNode] to request the Owner a new layout. [forceRequest] defines
//     * whether the node should bypass the logic that would reject relayout requests, and therefore
//     * force the relayout request to be evaluated even when it's already pending measure/layout.
//     *
//     * [affectsLookahead] specifies whether this relayout request is for the lookahead pass
//     * pass.
//     */
//    fun onRequestRelayout(
//        layoutNode: LayoutNode,
//        affectsLookahead: Boolean = false,
//        forceRequest: Boolean = false
//    )
//
//    /**
//     * Called when graphics layers have changed the position of children and the
//     * OnGloballyPositionedModifiers must be called.
//     */
//    fun requestOnPositionedCallback(layoutNode: LayoutNode)
//
//    /**
//     * Called by [LayoutNode] when it is attached to the view system and now has an owner.
//     * This is used by [Owner] to track which nodes are associated with it. It will only be
//     * called when [node] is not already attached to an owner.
//     */
//    fun onAttach(node: LayoutNode)
//
//    /**
//     * Called by [LayoutNode] when it is detached from the view system, such as during
//     * [LayoutNode.removeAt]. This will only be called for [node]s that are already
//     * [LayoutNode.attach]ed.
//     */
//    fun onDetach(node: LayoutNode)
//
//    /**
//     * Returns the position relative to the containing window of the [localPosition],
//     * the position relative to the [Owner]. If the [Owner] is rotated, scaled, or otherwise
//     * transformed relative to the window, this will not be a simple translation.
//     */
//    fun calculatePositionInWindow(localPosition: Offset): Offset
//
//    /**
//     * Returns the position relative to the [Owner] of the [positionInWindow],
//     * the position relative to the window. If the [Owner] is rotated, scaled, or otherwise
//     * transformed relative to the window, this will not be a simple translation.
//     */
//    fun calculateLocalPosition(positionInWindow: Offset): Offset
//
//    /**
//     * Ask the system to provide focus to this owner.
//     *
//     * @return true if the system granted focus to this owner. False otherwise.
//     */
//    fun requestFocus(): Boolean
//
//    /**
//     * Iterates through all LayoutNodes that have requested layout and measures and lays them out.
//     * If [sendPointerUpdate] is `true` then a simulated PointerEvent may be sent to update pointer
//     * input handlers.
//     */
//    fun measureAndLayout(sendPointerUpdate: Boolean = true)
//
//    /**
//     * Measures and lays out only the passed [layoutNode]. It will be remeasured with the passed
//     * [constraints].
//     */
//    fun measureAndLayout(layoutNode: LayoutNode, constraints: Constraints)
//
//    /**
//     * Makes sure the passed [layoutNode] and its subtree is remeasured and has the final sizes.
//     */
//    fun forceMeasureTheSubtree(layoutNode: LayoutNode)
//
//    /**
//     * Creates an [OwnedLayer] which will be drawing the passed [drawBlock].
//     */
//    fun createLayer(drawBlock: (Canvas) -> Unit, invalidateParentLayer: () -> Unit): OwnedLayer
//
//    /**
//     * The semantics have changed. This function will be called when a SemanticsNode is added to
//     * or deleted from the Semantics tree. It will also be called when a SemanticsNode in the
//     * Semantics tree has some property change.
//     */
//    fun onSemanticsChange()
//
//    /**
//     * The position and/or size of the [layoutNode] changed.
//     */
//    fun onLayoutChange(layoutNode: LayoutNode)
//
//    /**
//     * The [FocusDirection] represented by the specified keyEvent.
//     */
//    fun getFocusDirection(keyEvent: KeyEvent): FocusDirection?
//
//    val measureIteration: Long
//
//    /**
//     * The [ViewConfiguration] to use in the application.
//     */
//    val viewConfiguration: ViewConfiguration
//
//    /**
//     * Performs snapshot observation for blocks like draw and layout which should be re-invoked
//     * automatically when the snapshot value has been changed.
//     */
//    val snapshotObserver: OwnerSnapshotObserver
//
//    val modifierLocalManager: ModifierLocalManager
//
//    /**
//     * Registers a call to be made when the [Applier.onEndChanges] is called. [listener]
//     * should be called in [onEndApplyChanges] and then removed after being called.
//     */
//    fun registerOnEndApplyChangesListener(listener: () -> Unit)
//
//    /**
//     * Called when [Applier.onEndChanges] executes. This must call all listeners registered
//     * in [registerOnEndApplyChangesListener] and then remove them so that they are not
//     * called again.
//     */
//    fun onEndApplyChanges()
//
//    /**
//     * [listener] will be notified after the current or next layout has finished.
//     */
//    fun registerOnLayoutCompletedListener(listener: OnLayoutCompletedListener)
//
//    companion object {
//        /**
//         * Enables additional (and expensive to do in production) assertions. Useful to be set
//         * to true during the tests covering our core logic.
//         */
//        var enableExtraAssertions: Boolean = false
//    }
//
//    interface OnLayoutCompletedListener {
//        fun onLayoutComplete()
//    }
//}
//
///*** */
//private val View.contentChild: View
//    get() {
//        var self: View = this
//        var parent: ViewParent? = self.parent
//        while (parent is View) {
//            if (parent.id == android.R.id.content) return self
//            self = parent
//            parent = self.parent
//        }
//        return self
//    }
//
//@OptIn(InternalComposeUiApi::class)
//internal val View.windowRecomposer: Recomposer
//    get() {
//        check(isAttachedToWindow) {
//            "Cannot locate windowRecomposer; View $this is not attached to a window"
//        }
//        val rootView = contentChild
//        return when (val rootParentRef = rootView.compositionContext) {
//            null -> WindowRecomposerPolicy.createAndInstallWindowRecomposer(rootView)
//            is Recomposer -> rootParentRef
//            else -> error("root viewTreeParentCompositionContext is not a Recomposer")
//        }
//    }
//
//@InternalComposeUiApi
//object WindowRecomposerPolicy {
//
//    private val factory = AtomicReference<WindowRecomposerFactory>(
//        WindowRecomposerFactory.LifecycleAware
//    )
//
//    // Don't expose the actual AtomicReference as @PublishedApi; we might convert to atomicfu later
//    @Suppress("ShowingMemberInHiddenClass")
//    @PublishedApi
//    internal fun getAndSetFactory(
//        factory: WindowRecomposerFactory
//    ): WindowRecomposerFactory = this.factory.getAndSet(factory)
//
//    @Suppress("ShowingMemberInHiddenClass")
//    @PublishedApi
//    internal fun compareAndSetFactory(
//        expected: WindowRecomposerFactory,
//        factory: WindowRecomposerFactory
//    ): Boolean = this.factory.compareAndSet(expected, factory)
//
//    fun setFactory(factory: WindowRecomposerFactory) {
//        this.factory.set(factory)
//    }
//
//    inline fun <R> withFactory(
//        factory: WindowRecomposerFactory,
//        block: () -> R
//    ): R {
//        var cause: Throwable? = null
//        val oldFactory = getAndSetFactory(factory)
//        return try {
//            block()
//        } catch (t: Throwable) {
//            cause = t
//            throw t
//        } finally {
//            if (!compareAndSetFactory(factory, oldFactory)) {
//                val err = IllegalStateException(
//                    "WindowRecomposerFactory was set to unexpected value; cannot safely restore " +
//                            "old state"
//                )
//                if (cause == null) throw err
//                cause.addSuppressed(err)
//                throw cause
//            }
//        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    internal fun createAndInstallWindowRecomposer(rootView: View): Recomposer {
//        val newRecomposer = factory.get().createRecomposer(rootView)
//        rootView.compositionContext = newRecomposer
//
//        // If the Recomposer shuts down, unregister it so that a future request for a window
//        // recomposer will consult the factory for a new one.
//        val unsetJob = GlobalScope.launch(
//            rootView.handler.asCoroutineDispatcher("windowRecomposer cleanup").immediate
//        ) {
//            try {
//                newRecomposer.join()
//            } finally {
//                // Unset if the view is detached. (See below for the attach state change listener.)
//                // Since this is in a finally in this coroutine, even if this job is cancelled we
//                // will resume on the window's UI thread and perform this manipulation there.
//                val viewTagRecomposer = rootView.compositionContext
//                if (viewTagRecomposer === newRecomposer) {
//                    rootView.compositionContext = null
//                }
//            }
//        }
//
//        // If the root view is detached, cancel the await for recomposer shutdown above.
//        // This will also unset the tag reference to this recomposer during its cleanup.
//        rootView.addOnAttachStateChangeListener(
//            object : View.OnAttachStateChangeListener {
//                override fun onViewAttachedToWindow(v: View) {}
//                override fun onViewDetachedFromWindow(v: View) {
//                    v.removeOnAttachStateChangeListener(this)
//                    // cancel the job to clean up the view tags.
//                    // this will happen immediately since unsetJob is on an immediate dispatcher
//                    // for this view's UI thread instead of waiting for the recomposer to join.
//                    // NOTE: This does NOT cancel the returned recomposer itself, as it may be
//                    // a shared-instance recomposer that should remain running/is reused elsewhere.
//                    unsetJob.cancel()
//                }
//            }
//        )
//        return newRecomposer
//    }
//}