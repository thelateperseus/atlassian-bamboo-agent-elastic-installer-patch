# IMDSv2 Patch for Elastic Bamboo Installer

Adds support for IMDSv2 until [BAM-21978](https://jira.atlassian.com/browse/BAM-21978) is fixed.

To use:
1. Edit `build.gradle` to set the appropriate versions for `atlassian-aws-bootstrap` and `atlassian-bamboo-elastic-image`.
2. Run `gradlew clean build` to build the artifacts.
3. Use `build/distributions/atlassian-bamboo-elastic-image-8.4.zip` when [building your custom elastic image](https://confluence.atlassian.com/bamboo/creating-a-custom-elastic-image-289277146.html).
4. Copy `build/libs/atlassian-aws-bootstrap-1.0.188.jar` to an S3 bucket.
5. Create a startup script which downloads the patched `atlassian-aws-bootstrap-1.0.188.jar` from your S3 bucket. Refer to the workaround on [BAM-22098](https://jira.atlassian.com/browse/BAM-22098) for details on how to do this.
