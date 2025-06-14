#!/bin/bash
# if needed:
#wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/7.13.0/openapi-generator-cli-7.13.0.jar -O openapi-generator-cli.jar

OPENAPI_FILE="../src/main/resources/openapi.yml"
OUTPUT_BASE="../src/main/java"
TEMP_DIR="./tmp-openapi"

declare -a SLICES_AND_SPECS=(
  "coach=../src/main/resources/openapi/user-coach.yml"
  "client=../src/main/resources/openapi/user-client.yml"
)

for entry in "${SLICES_AND_SPECS[@]}"; do
  SLICE="${entry%%=*}"
  SPEC_FILE="${entry#*=}"
  rm -rf "$TEMP_DIR"
  PACKAGE="com.inigo.fitmentor.${SLICE}.infrastructure.api"
  OUTPUT_DIR="../src/main/java/$(echo $PACKAGE | tr '.' '/')"
  echo "Generating APIs for: '$SLICE'..."

  java -jar openapi-generator-cli.jar generate \
    -g kotlin-spring \
    -i "$SPEC_FILE" \
    -o "$TEMP_DIR" \
    --global-property=api,apis,models \
    --additional-properties=interfaceOnly=true,skipDefaultInterface=true,useTags=true,apiNameSuffix="$SLICE" \
    --package-name="$PACKAGE" \
    --skip-validate-spec

  mkdir -p "$OUTPUT_DIR"

  find ../src/main/java -type f -name "*.kt" -exec sed -i 's/import javax\./import jakarta./g' {} +
  find ../src/main/java -type f -name "*.kt" -exec sed -i '/^[[:space:]]*@field:Valid[[:space:]]*$/d' {} +
  find ../src/main/java -type f -name "*.kt" -exec sed -i -E '/@Schema\([^\)]*\)[[:space:]]*$/{
  N
  s/@Schema\([^\)]*\)[[:space:]]*\n[[:space:]]*@get:JsonProperty\("([^"]+)"[^\)]*\)/@field:NotNull(message = "\1 must not be null")/
  }' {} +

  rsync -a "$TEMP_DIR"/src/main/kotlin/* "$OUTPUT_BASE"
done

rm -rf "$TEMP_DIR"