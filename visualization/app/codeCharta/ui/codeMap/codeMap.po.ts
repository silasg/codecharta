import { clickButtonOnPageElement } from "../../../puppeteer.helper"

export class CodeMapPageObject {
    async clickMap() {
        await clickButtonOnPageElement("#codeMap", { button: "left" })
    }

    async rightClickMouseDownOnMap() {
        const codeMapElement = await page.waitForSelector("#codeMap")
        await codeMapElement.hover()

        await page.mouse.down({ button: "right" })
    }

    async mouseWheelWithinMap() {
        const codeMapElement = await page.waitForSelector("#codeMap")
        await codeMapElement.hover()

        // The wheel() type definition is not already provided
        // Use the wheel() function anyway
        // @ts-ignore
        await page.mouse.wheel({ deltaX: 100 })
    }
}
