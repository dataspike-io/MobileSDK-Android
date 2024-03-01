package io.dataspike.mobile_sdk.view.mappers

import io.dataspike.mobile_sdk.domain.models.UiConfigDomainModel
import io.dataspike.mobile_sdk.utils.lightenColor
import io.dataspike.mobile_sdk.utils.parseColorString
import io.dataspike.mobile_sdk.view.LIGHTER_FRACTION
import io.dataspike.mobile_sdk.view.LIGHT_FRACTION
import io.dataspike.mobile_sdk.view.ui_models.ButtonModel
import io.dataspike.mobile_sdk.view.ui_models.ButtonStyleModel
import io.dataspike.mobile_sdk.view.ui_models.ComponentsModel
import io.dataspike.mobile_sdk.view.ui_models.FontModel
import io.dataspike.mobile_sdk.view.ui_models.IntroLinkModel
import io.dataspike.mobile_sdk.view.ui_models.LinksModel
import io.dataspike.mobile_sdk.view.ui_models.MessagesModel
import io.dataspike.mobile_sdk.view.ui_models.OnboardingLinksModel
import io.dataspike.mobile_sdk.view.ui_models.OptionsModel
import io.dataspike.mobile_sdk.view.ui_models.PaletteModel
import io.dataspike.mobile_sdk.view.ui_models.RequirementsLinkModel
import io.dataspike.mobile_sdk.view.ui_models.ThemeModel
import io.dataspike.mobile_sdk.view.ui_models.TypographyModel
import io.dataspike.mobile_sdk.view.ui_models.UiConfigModel
import io.dataspike.mobile_sdk.view.ui_models.VerificationResultLinkModel

internal class UiConfigMapper {

    private fun getLink(link: String): String {
        return if (link.last() == '/') {
            link
        } else {
            "$link/"
        }
    }

    fun map(
        uiConfigDomainModel: UiConfigDomainModel?,
        darkModeIsEnabled: Boolean
    ): UiConfigModel {
        val defaultUiConfig = UiConfigModel.getConfig(darkModeIsEnabled)

        uiConfigDomainModel ?: return defaultUiConfig

        val mainColor: Int?
        val successColor: Int?
        val errorColor: Int?

        if (darkModeIsEnabled) {
            mainColor = parseColorString(uiConfigDomainModel.themes?.dark?.palette?.mainColor)
            successColor =
                parseColorString(uiConfigDomainModel.themes?.dark?.palette?.successColor)
            errorColor = parseColorString(uiConfigDomainModel.themes?.dark?.palette?.errorColor)
        } else {
            mainColor = parseColorString(uiConfigDomainModel.themes?.light?.palette?.mainColor)
            successColor =
                parseColorString(uiConfigDomainModel.themes?.light?.palette?.successColor)
            errorColor = parseColorString(uiConfigDomainModel.themes?.light?.palette?.errorColor)
        }

        val poiLink = getLink(
            uiConfigDomainModel.links?.requirements?.poi
            ?: defaultUiConfig.links.requirements.poi
        )
        val livenessLink = getLink(
            uiConfigDomainModel.links?.requirements?.liveness
            ?: defaultUiConfig.links.requirements.liveness
        )
        val poaLink = getLink(
            uiConfigDomainModel.links?.requirements?.poa
            ?: defaultUiConfig.links.requirements.poa
        )

        return UiConfigModel(
            theme = if (darkModeIsEnabled) {
                ThemeModel(
                    palette = PaletteModel(
                        backgroundColor =
                        parseColorString(uiConfigDomainModel.themes?.dark?.palette?.backgroundColor)
                            ?: ThemeModel.defaultDarkUITheme.palette.backgroundColor,
                        mainColor = mainColor
                            ?: ThemeModel.defaultDarkUITheme.palette.mainColor,
                        lightMainColor = lightenColor(mainColor, LIGHT_FRACTION)
                            ?: ThemeModel.defaultDarkUITheme.palette.lightMainColor,
                        lighterMainColor = lightenColor(mainColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultDarkUITheme.palette.lighterMainColor,
                        successColor = successColor
                            ?: ThemeModel.defaultDarkUITheme.palette.successColor,
                        lighterSuccessColor = lightenColor(successColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultDarkUITheme.palette.lighterSuccessColor,
                        errorColor = errorColor
                            ?: ThemeModel.defaultDarkUITheme.palette.errorColor,
                        lighterErrorColor = lightenColor(errorColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultDarkUITheme.palette.lighterErrorColor,
                    ),
                    typography = TypographyModel(
                        header = FontModel(
                            font = uiConfigDomainModel.themes?.dark?.typography?.header?.font
                                ?: ThemeModel.defaultDarkUITheme.typography.header.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.dark?.typography?.header?.textColor
                            ) ?: ThemeModel.defaultDarkUITheme.typography.header.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.dark?.typography?.header?.textSize
                                ?: ThemeModel.defaultDarkUITheme.typography.header.textSize,
                        ),
                        bodyOne = FontModel(
                            font = uiConfigDomainModel.themes?.dark?.typography?.bodyOne?.font
                                ?: ThemeModel.defaultDarkUITheme.typography.bodyOne.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.dark?.typography?.bodyOne?.textColor
                            ) ?: ThemeModel.defaultDarkUITheme.typography.bodyOne.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.dark?.typography?.bodyOne?.textSize
                                ?: ThemeModel.defaultDarkUITheme.typography.bodyOne.textSize,
                        ),
                        bodyTwo = FontModel(
                            font = uiConfigDomainModel.themes?.dark?.typography?.bodyTwo?.font
                                ?: ThemeModel.defaultDarkUITheme.typography.bodyTwo.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.dark?.typography?.bodyTwo?.textColor
                            ) ?: ThemeModel.defaultDarkUITheme.typography.bodyTwo.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.dark?.typography?.bodyTwo?.textSize
                                ?: ThemeModel.defaultDarkUITheme.typography.bodyTwo.textSize,
                        ),
                    ),
                )
            } else {
                ThemeModel(
                    palette = PaletteModel(
                        backgroundColor =
                        parseColorString(
                            uiConfigDomainModel.themes?.light?.palette?.backgroundColor
                        ) ?: ThemeModel.defaultLightUITheme.palette.backgroundColor,
                        mainColor = mainColor
                            ?: ThemeModel.defaultLightUITheme.palette.mainColor,
                        lightMainColor = lightenColor(mainColor, LIGHT_FRACTION)
                            ?: ThemeModel.defaultLightUITheme.palette.lightMainColor,
                        lighterMainColor = lightenColor(mainColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultLightUITheme.palette.lighterMainColor,
                        successColor = successColor
                            ?: ThemeModel.defaultLightUITheme.palette.successColor,
                        lighterSuccessColor = lightenColor(successColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultLightUITheme.palette.lighterSuccessColor,
                        errorColor = errorColor
                            ?: ThemeModel.defaultLightUITheme.palette.errorColor,
                        lighterErrorColor = lightenColor(errorColor, LIGHTER_FRACTION)
                            ?: ThemeModel.defaultLightUITheme.palette.lighterErrorColor,
                    ),
                    typography = TypographyModel(
                        header = FontModel(
                            font = uiConfigDomainModel.themes?.light?.typography?.header?.font
                                ?: ThemeModel.defaultLightUITheme.typography.header.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.light?.typography?.header?.textColor
                            ) ?: ThemeModel.defaultLightUITheme.typography.header.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.light?.typography?.header?.textSize
                                ?: ThemeModel.defaultLightUITheme.typography.header.textSize,
                        ),
                        bodyOne = FontModel(
                            font = uiConfigDomainModel.themes?.light?.typography?.bodyOne?.font
                                ?: ThemeModel.defaultLightUITheme.typography.bodyOne.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.light?.typography?.bodyOne?.textColor
                            ) ?: ThemeModel.defaultLightUITheme.typography.bodyOne.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.light?.typography?.bodyOne?.textSize
                                ?: ThemeModel.defaultLightUITheme.typography.bodyOne.textSize,
                        ),
                        bodyTwo = FontModel(
                            font = uiConfigDomainModel.themes?.light?.typography?.bodyTwo?.font
                                ?: ThemeModel.defaultLightUITheme.typography.bodyTwo.font,
                            textColor =
                            parseColorString(
                                uiConfigDomainModel.themes?.light?.typography?.bodyTwo?.textColor
                            ) ?: ThemeModel.defaultLightUITheme.typography.bodyTwo.textColor,
                            textSize =
                            uiConfigDomainModel.themes?.light?.typography?.bodyTwo?.textSize
                                ?: ThemeModel.defaultLightUITheme.typography.bodyTwo.textSize,
                        ),
                    ),
                )
            },
            components = ComponentsModel(
                button = ButtonModel(
                    style = ButtonStyleModel(
                        margin = uiConfigDomainModel.components?.button?.style?.margin
                            ?: defaultUiConfig.components.button.style.margin,
                        cornerRadius = uiConfigDomainModel.components?.button?.style?.cornerRadius
                            ?: defaultUiConfig.components.button.style.cornerRadius,
                    )
                )
            ),
            messages = MessagesModel(
                verificationSuccessful = uiConfigDomainModel.messages?.verificationSuccessful
                    ?: defaultUiConfig.messages.verificationSuccessful,
                verificationExpired = uiConfigDomainModel.messages?.verificationExpired
                    ?: defaultUiConfig.messages.verificationExpired,
                verificationFailed = uiConfigDomainModel.messages?.verificationFailed
                    ?: defaultUiConfig.messages.verificationFailed,
            ),
            links = LinksModel(
                onboarding = OnboardingLinksModel(
                    poi = uiConfigDomainModel.links?.onboarding?.poi
                        ?: defaultUiConfig.links.onboarding.poi,
                    liveness = uiConfigDomainModel.links?.onboarding?.liveness
                        ?: defaultUiConfig.links.onboarding.liveness,
                    poa = uiConfigDomainModel.links?.onboarding?.poa
                        ?: defaultUiConfig.links.onboarding.poa,
                ),
                verificationResult = VerificationResultLinkModel(
                    verificationSuccessful =
                    uiConfigDomainModel.links?.verificationResult?.verificationSuccessful
                        ?: defaultUiConfig.links.verificationResult.verificationSuccessful,
                    verificationExpired =
                    uiConfigDomainModel.links?.verificationResult?.verificationExpired
                        ?: defaultUiConfig.links.verificationResult.verificationExpired,
                    verificationFailed =
                    uiConfigDomainModel.links?.verificationResult?.verificationFailed
                        ?: defaultUiConfig.links.verificationResult.verificationFailed,
                ),
                intro = IntroLinkModel(
                    poi = uiConfigDomainModel.links?.intro?.poi
                        ?: defaultUiConfig.links.intro.poi,
                    poa = uiConfigDomainModel.links?.intro?.poa
                        ?: defaultUiConfig.links.intro.poa,
                ),
                requirements = RequirementsLinkModel(
                    poi = poiLink,
                    liveness = livenessLink,
                    poa = poaLink,
                )
            ),
            options = OptionsModel(
                showTimer = uiConfigDomainModel.options?.showTimer
                    ?: defaultUiConfig.options.showTimer,
                showSteps = uiConfigDomainModel.options?.showSteps
                    ?: defaultUiConfig.options.showSteps,
                disableDarkMode =
                uiConfigDomainModel.options?.disableDarkMode
                    ?: defaultUiConfig.options.disableDarkMode,
            ),
        )
    }
}