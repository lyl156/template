package _0_canary_sdk

// Contains checks whether a given element is present in the slice.
func Contains[T comparable](slice []T, value T) bool {
	for _, v := range slice {
		if v == value {
			return true
		}
	}
	return false
}

// ContainsAny checks whether any of the given elements are present in the slice.
func ContainsAny[T comparable](slice []T, values ...T) bool {
	for _, val := range values {
		if Contains(slice, val) {
			return true
		}
	}
	return false
}

// ContainsAll checks whether all of the given elements are present in the slice.
func ContainsAll[T comparable](slice []T, values ...T) bool {
	for _, val := range values {
		if !Contains(slice, val) {
			return false
		}
	}
	return true
}
