package com.autonomousapps.cappuccino.animations

/**
 * The plugin extension for Cappuccino Animations plugin. Any variant included
 * in this list will be ignored by the Cappuccino Animations plugin. By default,
 * the plugin already ignores the any variant with the 'Release' configuration.
 * Use it like:
 *
 * <pre>
 * cappuccino {
 *      excludedConfigurations = ['variantToNotCreateGrantAnimationTaskFor']
 * }
 * </pre>
 */
class CappuccinoAnimationsExtension {
    def excludedConfigurations = []
}