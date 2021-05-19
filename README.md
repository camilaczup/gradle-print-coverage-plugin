See https://github.com/jansauer/gradle-print-coverage-plugin

## Usage

```kotlin
jacocoTestReport {
    enabled = true
    reports {
        xml.isEnabled = true
    }
}

jacocoPrintCoverageStatus {
    finalizedBy(jacocoTestCoverageVerification)
}

jacocoTestCoverageVerification {
    violationRules {
        rule { limit { minimum = "0.7".toBigDecimal() } }
    }
}
```
