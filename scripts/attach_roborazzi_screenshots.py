#!/usr/bin/env python3
"""Attach Roborazzi screenshots to their matching JUnit test cases.

Neither Gradle's JUnit XML writer nor Roborazzi emit the
<property name="attachment_*" value="..."/> format Bitrise's
custom-test-results-export step reads to find test attachments. This
script bridges the two: Roborazzi's default naming strategy names each
screenshot "{fully-qualified-class}.{method}.png", which matches a
testcase's classname+name exactly, so no separate mapping is needed.
"""
import glob
import os
import shutil
import sys
import xml.etree.ElementTree as ET

JUNIT_GLOB = "app/build/test-results/testDebugUnitTest/TEST-*.xml"
SCREENSHOT_DIR = "app/build/outputs/roborazzi"
OUTPUT_DIR = "bitrise-test-results/roborazzi"


def main():
    junit_paths = glob.glob(JUNIT_GLOB)
    if not junit_paths:
        print(f"No JUnit XML found matching {JUNIT_GLOB}", file=sys.stderr)
        sys.exit(1)

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    attached_any = False

    for junit_path in junit_paths:
        tree = ET.parse(junit_path)
        root = tree.getroot()
        modified = False

        for testcase in root.findall("testcase"):
            classname = testcase.get("classname")
            name = testcase.get("name")
            screenshot_name = f"{classname}.{name}.png"
            screenshot_path = os.path.join(SCREENSHOT_DIR, screenshot_name)
            if not os.path.isfile(screenshot_path):
                continue

            properties = ET.SubElement(testcase, "properties")
            property_el = ET.SubElement(properties, "property")
            property_el.set("name", "attachment_0")
            property_el.set("value", screenshot_name)

            shutil.copy(screenshot_path, os.path.join(OUTPUT_DIR, screenshot_name))
            modified = True
            attached_any = True

        if modified:
            out_path = os.path.join(OUTPUT_DIR, os.path.basename(junit_path))
            tree.write(out_path, encoding="UTF-8", xml_declaration=True)
            print(f"Wrote {out_path} with attachment properties")

    if not attached_any:
        print("No screenshots matched any test case; nothing attached.")


if __name__ == "__main__":
    main()
