import { Toast } from 'vant';
import Vue from "vue";
//Register a global custom v-copy command `v-copy`.
const copy = {
  bind(el, { value }) {
    el.$value = value;
    el.handler = () => {
      el.style.position = 'relative';
      if (!el.$value) {
        //When the value is empty, a prompt will be given.
        Toast('No content');
        return
      }
      // Dynamically create text area labels
      const textarea = document.createElement('textarea');
      // Set the textarea to readonly to prevent the keyboard from automatically waking up under iOS, and at the same time move the textarea out of the visual area.
      textarea.readOnly = 'readonly';
      textarea.style.position = 'absolute';
      textarea.style.top = '0px';
      textarea.style.left = '-9999px';
      textarea.style.zIndex = '-9999';
      // Assign the value to be copied to the value property of textarea label.
      textarea.value = el.$value
      // Insert textarea into el
      el.appendChild(textarea);
      // Compatible IOS has no select () method.
      if (textarea.createTextRange) {
        textarea.select(); // Select the value and copy it.
      } else {
        textarea.setSelectionRange(0, el.$value.length);
        textarea.focus();
      }
      const result = document.execCommand('copy')
      if (result) Toast('Copy succeeded');
      el.removeChild(textarea);
    }
    el.addEventListener('click', el.handler); // Binding click event
  },
  // Triggered when the incoming value is updated.
  componentUpdated(el, { value }) {
    el.$value = value;
  },
  // When an instruction is unbound from an element, the event binding is removed.
  unbind(el) {
    el.removeEventListener('click', el.handler);
  },
}
export default copy